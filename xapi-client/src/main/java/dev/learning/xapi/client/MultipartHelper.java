/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Attachment;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.SubStatement;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;

/**
 * Helper methods for creating multipart message from statements.
 *
 * @author István Rátkai (Selindek)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MultipartHelper {

  private static final String MULTIPART_BOUNDARY = "xapi-learning-dev-boundary";
  private static final String MULTIPART_CONTENT_TYPE = "multipart/mixed; boundary="
      + MULTIPART_BOUNDARY;
  private static final String CRLF = "\r\n";
  private static final String BOUNDARY_PREFIX = "--";
  private static final String BODY_SEPARATOR = BOUNDARY_PREFIX + MULTIPART_BOUNDARY + CRLF;
  private static final String BODY_FOOTER = BOUNDARY_PREFIX + MULTIPART_BOUNDARY + BOUNDARY_PREFIX;

  public static final MediaType MULTIPART_MEDIATYPE = MediaType.valueOf(MULTIPART_CONTENT_TYPE);

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * <p>
   * Add a Statement to the request.
   * </p>
   * This method adds the statement and its attachments if there are any to the request body. Also
   * sets the content-type to multipart/mixed if needed.
   *
   * @param requestSpec a {@link RequestBodySpec} object.
   * @param statement   a {@link Statement} to add.
   */
  public static void addBody(RequestBodySpec requestSpec, Statement statement) {

    addBody(requestSpec, statement, getRealAttachments(statement));

  }

  /**
   * <p>
   * Adds a List of {@link Statement}s to the request.
   * </p>
   * This method adds the statements and their attachments if there are any to the request body.
   * Also sets the content-type to multipart/mixed if needed.
   *
   * @param requestSpec a {@link RequestBodySpec} object.
   * @param statement   list of {@link Statement}s to add.
   */
  public static void addBody(RequestBodySpec requestSpec, List<Statement> statements) {

    addBody(requestSpec, statements,
        statements.stream().flatMap(MultipartHelper::getRealAttachments));

  }

  public static void addBody(RequestBodySpec requestSpec, Object statements,
      Stream<Attachment> attachments) {

    final String attachmentsBody = writeAttachments(attachments);

    if (attachmentsBody.isEmpty()) {
      // add body directly, content-type is default application/json
      requestSpec.bodyValue(statements);
    } else {
      // has at least one attachment with actual data -> set content-type
      requestSpec.contentType(MULTIPART_MEDIATYPE);
      // construct whole multipart body manually
      requestSpec.bodyValue(createMultipartBody(statements, attachmentsBody));
    }

  }

  /**
   * Gets {@link Attachment}s of a {@link Statement} which has data property as a {@link Stream}.
   *
   * @param statement a {@link Statement} object
   * @return {@link Attachment} of a {@link Statement} which has data property as a {@link Stream}.
   */
  private static Stream<Attachment> getRealAttachments(Statement statement) {

    // handle the rare scenario when a sub-statement has an attachment
    Stream<Attachment> stream = statement.getObject() instanceof final SubStatement substatement
        && substatement.getAttachments() != null ? substatement.getAttachments().stream()
            : Stream.empty();

    if (statement.getAttachments() != null) {
      stream = Stream.concat(stream, statement.getAttachments().stream());
    }

    return stream.filter(a -> a.getData() != null);
  }

  @SneakyThrows
  private static String createMultipartBody(Object statements, String attachments) {
    final var body = new StringBuilder();
    // Multipart Boundary
    body.append(BODY_SEPARATOR);

    // Header of first part
    body.append(HttpHeaders.CONTENT_TYPE).append(':').append(MediaType.APPLICATION_JSON_VALUE)
        .append(CRLF);
    body.append(CRLF);

    // Body of first part
    body.append(objectMapper.writeValueAsString(statements)).append(CRLF);

    // Body of attachments
    body.append(attachments);

    // Footer
    body.append(BODY_FOOTER);

    return body.toString();
  }

  /*
   * Writes distinct attachments. If there are no attachments in the stream then returns an empty
   * String.
   */
  private static String writeAttachments(Stream<Attachment> attachments) {

    final var body = new StringBuilder();

    // Write identical attachments only once
    attachments.distinct().forEach(a -> {
      // Multipart Boundary
      body.append(BODY_SEPARATOR);

      // Multipart header
      body.append(HttpHeaders.CONTENT_TYPE).append(':').append(a.getContentType()).append(CRLF);
      body.append("Content-Transfer-Encoding:binary").append(CRLF);
      body.append("X-Experience-API-Hash:").append(a.getSha2()).append(CRLF);
      body.append(CRLF);

      // Multipart body
      if (MediaType.TEXT_PLAIN_VALUE.equals(a.getContentType())) {
        body.append(new String(a.getData())).append(CRLF);
      } else {
        body.append(Base64.decodeBase64(a.getData())).append(CRLF);
      }
    });

    return body.toString();
  }

}
