/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Statement;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Helper methods for creating multipart message from statements.
 *
 * @author István Rátkai (Selindek)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MultipartHelper {

  private static final String MULTIPART_BOUNDARY = "xapi.learning.dev";
  private static final String MULTIPART_CONTENT_TYPE = "multipart/mixed; boundary="
      + MULTIPART_BOUNDARY;
  private static final String CRLF = "\r\n";
  private static final String BOUNDARY_PREFIX = "--";
  private static final String BODY_SEPARATOR = BOUNDARY_PREFIX + MULTIPART_BOUNDARY + CRLF;
  private static final String BODY_END = BODY_SEPARATOR + BOUNDARY_PREFIX;

  public static final MediaType MULTIPART_MEDIATYPE = MediaType.valueOf(MULTIPART_CONTENT_TYPE);

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Checks if a {@link Statement} has at least one attachment with data.
   *
   * @param statement a {@link Statement} object
   * @return whether the {@link Statement} has at least attachment with data.
   */
  public static boolean hasAttachments(Statement statement) {
    final var attachments = statement.getAttachments();

    return attachments != null && Arrays.stream(attachments).anyMatch(a -> a.getData() != null);
  }

  /**
   * Checks if any {@link Statement}s in an array has at least one attachment with data.
   *
   * @param statement an array of {@link Statement}s
   * @return whether the any {@link Statement}s in an array has at least one attachment with data.
   */
  public static boolean hasAttachments(Statement[] statements) {

    return Arrays.stream(statements).filter(s -> s.getAttachments() != null)
        .flatMap(s -> Arrays.stream(s.getAttachments())).anyMatch(a -> a.getData() != null);
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
    // Header of first part
    body.append(HttpHeaders.CONTENT_TYPE).append(':').append(MediaType.APPLICATION_JSON_VALUE)
        .append(CRLF);
    body.append(CRLF);
    // Body of first part
    body.append(objectMapper.writeValueAsString(statement));

//    var getAttachments(statement)
//    writeAttachments(body, attachments);

    System.err.println(body.toString());
    return body.toString();
  }

  /**
   * Creates an xAPI multipart/mixed body from an array of {@link Statement}s.
   *
   * @param statement an array of {@link Statement}s.
   * @return the body as a String
   */
  public static String createMultipartBody(Statement[] statements) {
    final var body = new StringBuilder();

    return body.toString();
  }

}
