/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Adds xAPI response headers before controllers can commit a response, including errors. */
@Component
public class XapiResponseHeadersFilter extends OncePerRequestFilter {

  private final StatementConsistency consistency;

  /**
   * Creates the response header filter.
   *
   * @param consistency the statement retrieval boundary tracker
   */
  public XapiResponseHeadersFilter(StatementConsistency consistency) {
    this.consistency = consistency;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    final var path = request.getRequestURI().substring(request.getContextPath().length());
    if (path.equals("/xapi") || path.startsWith("/xapi/")) {
      response.setHeader("X-Experience-API-Version", "1.0.3");
      if (path.equals("/xapi/statements") || path.equals("/xapi/statements/")) {
        response.setHeader(
            "X-Experience-API-Consistent-Through", consistency.consistentThrough().toString());
      }
    }
    chain.doFilter(request, response);
  }
}
