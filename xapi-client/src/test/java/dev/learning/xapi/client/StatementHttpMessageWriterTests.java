/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Attachment;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.SubStatement;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CodecException;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/** Tests the writer's routing, multipart assembly and error propagation through its public API. */
@DisplayName("Statement HTTP message writer tests")
class StatementHttpMessageWriterTests {

  private static final Duration TIMEOUT = Duration.ofSeconds(5);

  private final JsonMapper mapper = JsonMapper.builder().build();

  private final StatementHttpMessageWriter writer =
      new StatementHttpMessageWriter(ClientCodecConfigurer.create().getWriters());

  @ParameterizedTest(name = "{0}")
  @MethodSource("jsonBodies")
  @DisplayName("Bodies without inline attachments retain their JSON shape")
  void whenNoInlineAttachmentsThenWriteJson(String description, Object body) {
    final var response = write(body, MediaType.APPLICATION_JSON);

    assertThat(response.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON));
    assertThat(
        mapper.readTree(response.getBodyAsString().block(TIMEOUT)),
        is(mapper.<JsonNode>valueToTree(body)));
  }

  private static Stream<Arguments> jsonBodies() {
    final var statement = statement();
    final var remote = attachment("remote").withContent(null);
    return Stream.of(
        Arguments.of("statement with null attachments", statement),
        Arguments.of("statement with empty attachments", statement.withAttachments(List.of())),
        Arguments.of("URL-only attachment", statement.withAttachments(List.of(remote))),
        Arguments.of(
            "sub-statement without attachments", statement.withObject(subStatement().build())),
        Arguments.of("list of statements", List.of(statement)),
        Arguments.of("empty list", List.of()),
        Arguments.of("non-statement list", List.of("first", "second")),
        Arguments.of("non-statement object", Map.of("state", "completed")));
  }

  @Test
  @DisplayName("Non-statement text is delegated to a compatible text writer")
  void whenWritingTextThenUseTextWriter() {
    final var response = write("plain text", MediaType.TEXT_PLAIN);

    assertThat(
        response.getHeaders().getContentType().isCompatibleWith(MediaType.TEXT_PLAIN), is(true));
    assertThat(response.getBodyAsString().block(TIMEOUT), is("plain text"));
  }

  @Test
  @DisplayName("Nested and outer attachments are combined, filtered and deduplicated")
  void whenNestedAndOuterAttachmentsThenWriteEachInlineAttachmentOnce() {
    final var shared = attachment("shared");
    final var nested = attachment("nested");
    final var outer = attachment("outer");
    final var statement =
        statement()
            .withObject(
                subStatement()
                    // A URL-only reference must not hide a later inline copy of the same hash.
                    .addAttachment(shared.withContent(null))
                    .addAttachment(nested)
                    .build())
            .withAttachments(
                List.of(shared, nested, outer, attachment("remote").withContent(null)));

    assertMultipart(statement, List.of(nested, shared, outer));
  }

  @Test
  @DisplayName("An attachment in a later statement still triggers multipart output")
  void whenOnlyLaterStatementHasAttachmentThenWriteMultipart() {
    final var attachment = attachment("later statement");
    final var statements = List.of(statement(), statement().withAttachments(List.of(attachment)));

    assertMultipart(statements, List.of(attachment));
  }

  @Test
  @DisplayName("An empty inline file is sent as an attachment, rather than omitted")
  void whenAttachmentHasZeroBytesThenStillWriteMultipartPart() {
    final var attachment = attachment("");

    assertMultipart(statement().withAttachments(List.of(attachment)), List.of(attachment));
  }

  @Test
  @DisplayName("Missing statement codecs produce a useful multipart encoding error")
  void whenNoWriterForJsonPartThenFailWithCodecException() {
    final var unsupportedWriter = new StatementHttpMessageWriter(List.of());
    final var statement = statement().withAttachments(List.of(attachment("data")));
    final var response = new MockServerHttpResponse();

    final var exception =
        assertThrows(
            CodecException.class,
            () ->
                unsupportedWriter
                    .write(
                        Mono.just(statement),
                        ResolvableType.forInstance(statement),
                        MediaType.APPLICATION_JSON,
                        response,
                        Map.of())
                    .block(TIMEOUT));

    assertThat(exception.getMessage(), containsString("No suitable writer found for part:"));
    assertThat(exception.getMessage(), containsString(Statement.class.getName()));
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  @DisplayName("Part preparation and body streaming failures reach the caller unchanged")
  void whenPartWriterFailsThenPropagateFailure(boolean failDuringStreaming) {
    @SuppressWarnings("unchecked")
    final var partWriter = (HttpMessageWriter<Object>) mock(HttpMessageWriter.class);
    final var failure = new IllegalStateException("Cannot encode statement");
    when(partWriter.canWrite(any(), any())).thenReturn(true);
    when(partWriter.write(any(), any(), any(), any(), any()))
        .thenAnswer(
            invocation -> {
              final ReactiveHttpOutputMessage message = invocation.getArgument(3);
              return failDuringStreaming
                  ? Mono.defer(() -> message.writeWith(Mono.error(failure)))
                  : Mono.error(failure);
            });
    final var failingWriter = new StatementHttpMessageWriter(List.of(partWriter));
    final var statement = statement().withAttachments(List.of(attachment("data")));
    final var response = new MockServerHttpResponse();

    final var exception =
        assertThrows(
            IllegalStateException.class,
            () ->
                failingWriter
                    .write(
                        Mono.just(statement),
                        ResolvableType.forInstance(statement),
                        MediaType.APPLICATION_JSON,
                        response,
                        Map.of())
                    .block(TIMEOUT));

    assertSame(failure, exception);
  }

  private MockServerHttpResponse write(Object body, MediaType mediaType) {
    final var response = new MockServerHttpResponse();
    writer
        .write(Mono.just(body), ResolvableType.forInstance(body), mediaType, response, Map.of())
        .block(TIMEOUT);
    return response;
  }

  private void assertMultipart(Object body, List<Attachment> attachments) {
    final var response = write(body, MediaType.APPLICATION_JSON);
    final var contentType = response.getHeaders().getContentType();
    assertThat(contentType.isCompatibleWith(MediaType.MULTIPART_MIXED), is(true));
    final var boundary = contentType.getParameter("boundary");
    final var parts =
        Arrays.asList(
            response.getBodyAsString().block(TIMEOUT).split(Pattern.quote("--" + boundary), -1));

    assertThat(parts.size(), is(attachments.size() + 3));
    assertThat(parts.getFirst(), is(""));
    assertThat(parts.getLast(), is("--\r\n"));
    final var jsonPart = parts.get(1).split("\r\n\r\n", 2);
    assertThat(jsonPart[0], containsString("Content-Type: application/json"));
    assertThat(mapper.readTree(jsonPart[1]), is(mapper.<JsonNode>valueToTree(body)));
    for (int i = 0; i < attachments.size(); i++) {
      final var attachment = attachments.get(i);
      assertThat(
          parts.get(i + 2),
          is(
              "\r\nContent-Type: text/plain\r\n"
                  + "Content-Transfer-Encoding: binary\r\n"
                  + "X-Experience-API-Hash: "
                  + attachment.getSha2()
                  + "\r\n\r\n"
                  + new String(attachment.getContent(), StandardCharsets.UTF_8)
                  + "\r\n"));
    }
  }

  private static Statement statement() {
    return Statement.builder()
        .agentActor(a -> a.mbox("mailto:learner@example.com"))
        .verb(Verb.COMPLETED)
        .activityObject(a -> a.id("https://example.com/activity"))
        .build();
  }

  private static SubStatement.Builder subStatement() {
    return SubStatement.builder()
        .actor(statement().getActor())
        .verb(Verb.ATTEMPTED)
        .object(Activity.builder().id("https://example.com/activity").build());
  }

  private static Attachment attachment(String content) {
    return Attachment.builder()
        .usageType(URI.create("https://example.com/attachment"))
        .addDisplay(Locale.ENGLISH, "Attachment")
        .contentType("text/plain")
        .content(content)
        .length(content.getBytes(StandardCharsets.UTF_8).length)
        .fileUrl(URI.create("https://example.com/attachment.txt"))
        .build();
  }
}
