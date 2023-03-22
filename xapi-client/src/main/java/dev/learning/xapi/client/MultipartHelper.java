/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Attachment;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.SubStatement;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FastByteArrayOutputStream;
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
  private static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE + ":";

  private static final byte[] BA_APP_JSON_HEADER = (CONTENT_TYPE + MediaType.APPLICATION_JSON_VALUE
      + CRLF + CRLF).getBytes(StandardCharsets.UTF_8);
  private static final byte[] BA_CRLF = CRLF.getBytes(StandardCharsets.UTF_8);
  private static final byte[] BA_BODY_SEPARATOR = BODY_SEPARATOR.getBytes(StandardCharsets.UTF_8);
  private static final byte[] BA_BODY_FOOTER = BODY_FOOTER.getBytes(StandardCharsets.UTF_8);
  private static final byte[] BA_CONTENT_TYPE = CONTENT_TYPE.getBytes(StandardCharsets.UTF_8);
  private static final byte[] BA_ENCODING_HEADER = ("Content-Transfer-Encoding:binary" + CRLF)
      .getBytes(StandardCharsets.UTF_8);
  private static final byte[] BA_X_API_HASH = "X-Experience-API-Hash:"
      .getBytes(StandardCharsets.UTF_8);

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
   * @param statements  list of {@link Statement}s to add.
   */
  public static void addBody(RequestBodySpec requestSpec, List<Statement> statements) {

    addBody(requestSpec, statements,
        statements.stream().flatMap(MultipartHelper::getRealAttachments));

  }

  private static void addBody(RequestBodySpec requestSpec, Object statements,
      Stream<Attachment> attachments) {

    final var attachmentsBody = writeAttachments(attachments);

    if (attachmentsBody.length == 0) {
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

    return stream.filter(a -> a.getContent() != null);
  }

  private static byte[] createMultipartBody(Object statements, byte[] attachments) {

    try (var stream = new FastByteArrayOutputStream()) {
      // Multipart Boundary
      stream.write(BA_BODY_SEPARATOR);

      // Header of first part
      stream.write(BA_APP_JSON_HEADER);

      // Body of first part
      stream.write(objectMapper.writeValueAsBytes(statements));
      stream.write(BA_CRLF);

      // Body of attachments
      stream.write(attachments);

      // Footer
      stream.write(BA_BODY_FOOTER);

      return stream.toByteArrayUnsafe();
    } catch (final IOException e) {
      // should never happen
      throw new RuntimeException(e);
    }
  }

  /*
   * Writes attachments to a byte array. If there are no attachments in the stream then returns an
   * empty array.
   */
  private static byte[] writeAttachments(Stream<Attachment> attachments) {

    try (var stream = new FastByteArrayOutputStream()) {

      // Write each sha2-identical attachments only once
      attachments.collect(Collectors.toMap(Attachment::getSha2, v -> v, (k, v) -> v)).values()
          .forEach(a -> {
            try {
              // Multipart Boundary
              stream.write(BA_BODY_SEPARATOR);

              // Multipart headers
              stream.write(BA_CONTENT_TYPE);
              stream.write(a.getContentType().getBytes(StandardCharsets.UTF_8));
              stream.write(BA_CRLF);

              stream.write(BA_ENCODING_HEADER);

              stream.write(BA_X_API_HASH);
              stream.write(a.getSha2().getBytes(StandardCharsets.UTF_8));
              stream.write(BA_CRLF);
              stream.write(BA_CRLF);

              // Multipart body
              stream.write(a.getContent());
              stream.write(BA_CRLF);
            } catch (final IOException e) {
              // should never happen
              throw new RuntimeException(e);
            }

          });

      return stream.toByteArrayUnsafe();
    }
  }

}
