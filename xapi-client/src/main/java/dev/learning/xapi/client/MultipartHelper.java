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
   * Gets {@link Attachment} of a {@link Statement} which has data property as a {@link Stream}.
   *
   * @param statement a {@link Statement} object
   * @return {@link Attachment} of a {@link Statement} which has data property as a {@link Stream}.
   */
  public static Stream<Attachment> getRealAttachments(Statement statement) {

    // handle the rare scenario when a sub-statement has an attachment
    Stream<Attachment> stream = statement.getObject() instanceof final SubStatement substatement
        && substatement.getAttachments() != null ? substatement.getAttachments().stream()
            : Stream.empty();

    if (statement.getAttachments() != null) {
      stream = Stream.concat(stream, statement.getAttachments().stream());
    }

    return stream.filter(a -> a.getData() != null);
  }

  /**
   * Checks if a {@link Statement} has at least one attachment with data property.
   *
   * @param statement a {@link Statement} object
   * @return whether the {@link Statement} has at least attachment with data property.
   */
  public static boolean hasRealAttachments(Statement statement) {

    return getRealAttachments(statement).findAny().isPresent();
  }

  /**
   * Checks if any {@link Statement}s in a list has at least one attachment with data property.
   *
   * @param statement a list of {@link Statement}s
   * @return whether any {@link Statement}s in the list has at least one attachment with data
   *         property.
   */
  public static boolean hasRealAttachments(List<Statement> statements) {

    return statements.stream().anyMatch(MultipartHelper::hasRealAttachments);
  }

  /**
   * Creates an xAPI multipart/mixed body from a {@link Statement}.
   *
   * @param statement a {@link Statement} object.
   * @return the body as a String
   */
  @SneakyThrows
  public static String createMultipartBody(Statement statement) {
    var body = new StringBuilder();
    // Multipart Boundary
    body.append(BODY_SEPARATOR);

    // Header of first part
    body.append(HttpHeaders.CONTENT_TYPE).append(':').append(MediaType.APPLICATION_JSON_VALUE)
        .append(CRLF);
    body.append(CRLF);

    // Body of first part
    body.append(objectMapper.writeValueAsString(statement)).append(CRLF);

    // Body of attachments
    writeAttachments(body, getRealAttachments(statement));

    // footer
    body.append(BODY_FOOTER);

    return body.toString();
  }

  /**
   * Creates an xAPI multipart/mixed body from a list of {@link Statement}s.
   *
   * @param statements a list of {@link Statement}s.
   * @return the body as a String
   */
  @SneakyThrows
  public static String createMultipartBody(List<Statement> statements) {
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
    writeAttachments(body, statements.stream().flatMap(MultipartHelper::getRealAttachments));

    // Footer
    body.append(BODY_FOOTER);

    System.err.println(body.toString());
    return body.toString();
  }

  private static void writeAttachments(StringBuilder body, Stream<Attachment> attachments) {

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

  }

}
