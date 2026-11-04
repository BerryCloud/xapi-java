/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.OncePerRequestFilter;

/** Translates xAPI 1.0 alternate form requests before normal HTTP request handling. */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class AlternateRequestSyntaxFilter extends OncePerRequestFilter {

  private static final Set<String> METHODS =
      Set.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    final var path = request.getRequestURI().substring(request.getContextPath().length());
    return !path.equals("/xapi") && !path.startsWith("/xapi/");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    final MultiValueMap<String, String> query;
    try {
      query = decodeForm(request.getQueryString());
    } catch (IllegalArgumentException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed query string");
      return;
    }
    if (!query.containsKey("method")) {
      chain.doFilter(request, response);
      return;
    }
    if (!"POST".equals(request.getMethod())
        || query.size() != 1
        || query.get("method").size() != 1
        || !METHODS.contains(query.getFirst("method"))) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid alternate request method");
      return;
    }
    final AlternateRequestWrapper translated;
    try {
      final var form =
          decodeForm(new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
      translated = new AlternateRequestWrapper(request, query.getFirst("method"), form);
    } catch (IllegalArgumentException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid alternate request form");
      return;
    }
    chain.doFilter(translated, response);
  }

  private static MultiValueMap<String, String> decodeForm(String encoded) {
    final var result = new LinkedMultiValueMap<String, String>();
    if (encoded != null && !encoded.isEmpty()) {
      for (String field : encoded.split("&", -1)) {
        final var pair = field.split("=", 2);
        result.add(
            URLDecoder.decode(pair[0], StandardCharsets.UTF_8),
            pair.length == 2 ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "");
      }
    }
    return result;
  }
}
