/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class XapiResponseHeadersFilterTest {

  private static final String VERSION = "X-Experience-API-Version";
  private static final String CONSISTENCY = "X-Experience-API-Consistent-Through";
  private static final Instant NOW = Instant.parse("2026-10-15T12:00:00Z");
  private final XapiResponseHeadersFilter filter =
      new XapiResponseHeadersFilter(new StatementConsistency(Clock.fixed(NOW, ZoneOffset.UTC)));

  @Test
  void statementHeadersAreSetBeforeTheResponseIsCommittedForEveryHttpMethod() throws Exception {
    for (String method : new String[] {"GET", "HEAD", "PUT", "POST", "DELETE", "OPTIONS"}) {
      final var request = new MockHttpServletRequest(method, "/xapi/statements");
      final var response = new MockHttpServletResponse();
      filter.doFilter(
          request,
          response,
          (req, res) -> {
            assertEquals("1.0.3", response.getHeader(VERSION));
            assertEquals(NOW, Instant.parse(response.getHeader(CONSISTENCY)));
            response.flushBuffer();
          });
      assertEquals("1.0.3", response.getHeader(VERSION));
      assertEquals(NOW.toString(), response.getHeader(CONSISTENCY));
    }
  }

  @Test
  void headersSurviveAnErrorWithoutAResponseBody() throws Exception {
    final var request = new MockHttpServletRequest("GET", "/xapi/statements");
    final var response = new MockHttpServletResponse();
    filter.doFilter(request, response, (req, res) -> response.sendError(400));
    assertEquals(400, response.getStatus());
    assertEquals("1.0.3", response.getHeader(VERSION));
    assertEquals(NOW.toString(), response.getHeader(CONSISTENCY));
  }

  @Test
  void versionHeaderAppliesToOtherXapiResourcesWithoutAStatementBoundary() throws Exception {
    for (String path : new String[] {"/xapi/about", "/xapi/activities/state", "/xapi/missing"}) {
      final var response = new MockHttpServletResponse();
      filter.doFilter(new MockHttpServletRequest("GET", path), response, (req, res) -> {});
      assertEquals("1.0.3", response.getHeader(VERSION));
      assertNull(response.getHeader(CONSISTENCY));
    }
  }

  @Test
  void unrelatedPathsDoNotReceiveXapiHeaders() throws Exception {
    for (String path : new String[] {"/", "/xapi-other", "/error"}) {
      final var response = new MockHttpServletResponse();
      filter.doFilter(new MockHttpServletRequest("GET", path), response, (req, res) -> {});
      assertNull(response.getHeader(VERSION));
      assertNull(response.getHeader(CONSISTENCY));
    }
  }

  @Test
  void contextPathAndTrailingSlashAreSupported() throws Exception {
    final var request = new MockHttpServletRequest("GET", "/lrs/xapi/statements/");
    request.setContextPath("/lrs");
    final var response = new MockHttpServletResponse();
    filter.doFilter(request, response, (req, res) -> {});
    assertEquals("1.0.3", response.getHeader(VERSION));
    assertEquals(NOW.toString(), response.getHeader(CONSISTENCY));
  }
}
