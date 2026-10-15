/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

/** Presents the decoded form as an ordinary request to controllers and subsequent filters. */
final class AlternateRequestWrapper extends HttpServletRequestWrapper {

  private static final Set<String> FORM_HEADERS =
      Set.of(
          "authorization",
          "x-experience-api-version",
          "content-type",
          "content-length",
          "if-match",
          "if-none-match");

  private final String method;
  private final HttpHeaders headers = new HttpHeaders();
  private final Map<String, String[]> parameters = new LinkedHashMap<>();
  private final byte[] content;
  private final ServletInputStream input;

  AlternateRequestWrapper(
      HttpServletRequest request, String method, MultiValueMap<String, String> form) {
    super(request);
    this.method = method;
    Collections.list(request.getHeaderNames())
        .forEach(name -> headers.put(name, Collections.list(request.getHeaders(name))));
    form.forEach(
        (name, values) -> {
          if (FORM_HEADERS.contains(name.toLowerCase(java.util.Locale.ROOT))) {
            if (values.size() != 1) {
              throw new IllegalArgumentException("Repeated header");
            }
            headers.put(name, values);
          } else if (!name.equals("content")) {
            parameters.put(name, values.toArray(String[]::new));
          }
        });
    if ((method.equals("PUT") || method.equals("POST")) && !form.containsKey("content")) {
      throw new IllegalArgumentException("Missing content");
    }
    if (form.containsKey("content") && form.get("content").size() != 1) {
      throw new IllegalArgumentException("Repeated content");
    }
    content =
        form.getFirst("content") == null
            ? new byte[0]
            : form.getFirst("content").getBytes(StandardCharsets.UTF_8);
    // The wire headers describe the envelope, not the decoded content.
    if (!form.keySet().stream().anyMatch("Content-Type"::equalsIgnoreCase)) {
      headers.setContentType(MediaType.APPLICATION_JSON);
    }
    if (form.keySet().stream().anyMatch("Content-Length"::equalsIgnoreCase)
        && headers.getContentLength() != content.length) {
      throw new IllegalArgumentException("Content-Length does not match UTF-8 content");
    }
    headers.setContentLength(content.length);
    input =
        new ServletInputStream() {
          private final ByteArrayInputStream bytes = new ByteArrayInputStream(content);

          @Override
          public int read() {
            return bytes.read();
          }

          @Override
          public boolean isFinished() {
            return bytes.available() == 0;
          }

          @Override
          public boolean isReady() {
            return true;
          }

          @Override
          public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException("Only synchronous reads are supported");
          }
        };
  }

  @Override
  public String getMethod() {
    return method;
  }

  @Override
  public String getQueryString() {
    return null;
  }

  @Override
  public String getParameter(String name) {
    final var values = parameters.get(name);
    return values == null ? null : values[0];
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    return Collections.unmodifiableMap(parameters);
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return Collections.enumeration(parameters.keySet());
  }

  @Override
  public String[] getParameterValues(String name) {
    return parameters.get(name);
  }

  @Override
  public String getHeader(String name) {
    return headers.getFirst(name);
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    return Collections.enumeration(headers.getOrEmpty(name));
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    return Collections.enumeration(headers.headerNames());
  }

  @Override
  public int getIntHeader(String name) {
    final var value = getHeader(name);
    return value == null ? -1 : Integer.parseInt(value);
  }

  @Override
  public String getContentType() {
    return getHeader(HttpHeaders.CONTENT_TYPE);
  }

  @Override
  public int getContentLength() {
    return content.length;
  }

  @Override
  public long getContentLengthLong() {
    return content.length;
  }

  @Override
  public String getCharacterEncoding() {
    return StandardCharsets.UTF_8.name();
  }

  @Override
  public ServletInputStream getInputStream() {
    return input;
  }

  @Override
  public BufferedReader getReader() {
    return new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
  }
}
