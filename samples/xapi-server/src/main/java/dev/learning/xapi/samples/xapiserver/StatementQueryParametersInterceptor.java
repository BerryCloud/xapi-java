/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

/** Validates statement query parameter names and combinations before controller invocation. */
public class StatementQueryParametersInterceptor implements HandlerInterceptor {

  private static final Set<String> GET_PARAMETERS =
      Set.of(
          "statementId",
          "voidedStatementId",
          "agent",
          "verb",
          "activity",
          "registration",
          "related_activities",
          "related_agents",
          "since",
          "until",
          "limit",
          "format",
          "attachments",
          "ascending");

  private static final Set<String> SINGLE_STATEMENT_PARAMETERS = Set.of("format", "attachments");

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    final var parameters = request.getParameterMap().keySet();
    final var allowed =
        switch (request.getMethod()) {
          case "GET", "HEAD" -> GET_PARAMETERS;
          case "PUT" -> Set.of("statementId");
          default -> Set.<String>of();
        };
    if (!allowed.containsAll(parameters)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unrecognised statement parameter");
    }
    if (request.getMethod().equals("GET") || request.getMethod().equals("HEAD")) {
      for (final var id : List.of("statementId", "voidedStatementId")) {
        if (parameters.contains(id)
            && parameters.stream()
                .anyMatch(
                    name -> !name.equals(id) && !SINGLE_STATEMENT_PARAMETERS.contains(name))) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, id + " may only be combined with format and attachments");
        }
      }
    }
    return true;
  }
}
