/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AlternateRequestSyntaxFilterTest {

  private final AlternateRequestSyntaxFilter filter = new AlternateRequestSyntaxFilter();

  @Test
  void translatesHeadersParametersAndUtf8ContentBeforeTheChainRuns() throws Exception {
    String content = "{\"text\":\"café + 日本語 & = %\"}";
    var request =
        request(
            "POST",
            "method=PUT",
            "content="
                + encode(content)
                + "&statementId=123&tag=one&tag=two&Authorization=Basic+abc"
                + "&x-experience-api-version=1.0.3&Content-Type=application%2Fjson"
                + "&If-Match=%22etag%22&If-None-Match=*&Content-Length="
                + content.getBytes(StandardCharsets.UTF_8).length);
    request.addHeader("Authorization", "old");
    request.addHeader("Accept-Language", "en-GB");
    filter.doFilter(
        request,
        new MockHttpServletResponse(),
        (req, res) -> {
          var translated = (HttpServletRequest) req;
          assertEquals("PUT", translated.getMethod());
          assertEquals("Basic abc", translated.getHeader("authorization"));
          assertEquals("1.0.3", translated.getHeader("X-Experience-API-Version"));
          assertEquals("\"etag\"", translated.getHeader("if-match"));
          assertEquals("*", translated.getHeader("If-None-Match"));
          assertEquals("en-GB", translated.getHeader("Accept-Language"));
          assertEquals("application/json", translated.getContentType());
          assertEquals("UTF-8", translated.getCharacterEncoding());
          assertEquals(
              content.getBytes(StandardCharsets.UTF_8).length, translated.getContentLength());
          assertEquals(translated.getContentLength(), translated.getContentLengthLong());
          assertEquals(translated.getContentLength(), translated.getIntHeader("Content-Length"));
          assertEquals("123", translated.getParameter("statementId"));
          assertArrayEquals(new String[] {"one", "two"}, translated.getParameterValues("tag"));
          assertEquals(2, translated.getParameterMap().size());
          assertEquals(2, Collections.list(translated.getParameterNames()).size());
          assertNull(translated.getParameter("content"));
          assertNull(translated.getParameter("Authorization"));
          assertNull(translated.getParameter("method"));
          assertEquals(
              Collections.singletonList("Basic abc"),
              Collections.list(translated.getHeaders("AUTHORIZATION")));
          assertTrue(Collections.list(translated.getHeaderNames()).contains("If-Match"));
          assertEquals(content, translated.getReader().readLine());
          assertTrue(translated.getInputStream().isFinished());
        });
  }

  @Test
  void emptyGetFormDoesNotExposeTheEnvelopeAsContent() throws Exception {
    filter.doFilter(
        request("POST", "method=GET", "limit=1"),
        new MockHttpServletResponse(),
        (req, res) -> {
          var translated = (HttpServletRequest) req;
          assertEquals("GET", translated.getMethod());
          assertEquals("1", translated.getParameter("limit"));
          assertEquals(0, translated.getContentLength());
          assertEquals("0", translated.getHeader("Content-Length"));
          assertEquals(-1, translated.getInputStream().read());
        });
  }

  @Test
  void rejectsInvalidEnvelopesWithoutCallingTheChain() throws Exception {
    for (String query :
        new String[] {
          "method=PUT&statementId=123",
          "method=GET&method=PUT",
          "method=",
          "method=PATCH",
          "method=get",
          "method=%ZZ"
        }) {
      assertBadRequest(request("POST", query, ""));
    }
    assertBadRequest(request("PUT", "method=POST", "content=%7B%7D"));
    assertBadRequest(request("GET", "method=GET", ""));
    assertBadRequest(request("POST", "method=PUT", "content=%ZZ"));
    assertBadRequest(request("POST", "method=PUT", "statementId=123"));
    assertBadRequest(request("POST", "method=POST", ""));
    assertBadRequest(request("POST", "method=PUT", "content=abc&Content-Length=1"));
    assertBadRequest(request("POST", "method=PUT", "content=abc&Content-Length=bad"));
    assertBadRequest(request("POST", "method=PUT", "content=a&content=b"));
    assertBadRequest(request("POST", "method=GET", "Authorization=a&Authorization=b"));
  }

  @Test
  void leavesOrdinaryRequestsAndUnrelatedPathsUntouched() throws Exception {
    var ordinary = request("POST", "statementId=123", "unmodified body");
    var unrelated = request("POST", "method=GET", "unmodified body");
    unrelated.setRequestURI("/xapi-other");
    for (var request : new MockHttpServletRequest[] {ordinary, unrelated}) {
      var called = new AtomicBoolean();
      filter.doFilter(
          request,
          new MockHttpServletResponse(),
          (req, res) -> {
            called.set(true);
            assertSame(request, req);
            assertEquals(
                "unmodified body",
                new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
          });
      assertTrue(called.get());
    }
  }

  @Test
  void supportsContextPaths() throws Exception {
    var request = request("POST", "method=DELETE", "stateId=progress");
    request.setContextPath("/lrs");
    request.setRequestURI("/lrs/xapi/activities/state");
    filter.doFilter(
        request,
        new MockHttpServletResponse(),
        (req, res) -> {
          assertEquals("DELETE", ((HttpServletRequest) req).getMethod());
          assertEquals("progress", req.getParameter("stateId"));
        });
  }

  private void assertBadRequest(MockHttpServletRequest request) throws Exception {
    var response = new MockHttpServletResponse();
    filter.doFilter(request, response, (req, res) -> fail("Rejected request reached the chain"));
    assertEquals(400, response.getStatus());
  }

  private static MockHttpServletRequest request(String method, String query, String body) {
    var request = new MockHttpServletRequest(method, "/xapi/statements");
    request.setQueryString(query);
    request.setContentType("application/x-www-form-urlencoded");
    request.setContent(body.getBytes(StandardCharsets.UTF_8));
    return request;
  }

  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
