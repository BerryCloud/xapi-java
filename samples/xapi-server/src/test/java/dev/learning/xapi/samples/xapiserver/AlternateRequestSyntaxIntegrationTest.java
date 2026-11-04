/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
class AlternateRequestSyntaxIntegrationTest {

  @Autowired private MockMvc mvc;

  @Test
  void storesAndRetrievesAStatementThroughTheExistingControllers() throws Exception {
    var id = UUID.randomUUID();
    var statement =
        "{\"actor\":{\"mbox\":\"mailto:alternate@example.com\"},"
            + "\"verb\":{\"id\":\"https://example.com/verb\"},"
            + "\"object\":{\"id\":\"https://example.com/activity\"}}";
    mvc.perform(
            alternate(
                "/xapi/statements",
                "PUT",
                "statementId="
                    + id
                    + "&Content-Type=application%2Fjson&content="
                    + encode(statement)))
        .andExpect(status().isNoContent());
    mvc.perform(alternate("/xapi/statements", "GET", "statementId=" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()));
    mvc.perform(get("/xapi/statements").param("statementId", id.toString()))
        .andExpect(status().isOk());
    mvc.perform(alternate("/xapi/statements", "POST", "content=" + encode(statement)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").isString());
  }

  @Test
  void supportsStateContentAndConditionalHeadersForPutPostGetAndDelete() throws Exception {
    var endpoint = "/xapi/activities/state";
    var parameters =
        "activityId="
            + encode("https://example.com/alternate")
            + "&agent="
            + encode("{\"mbox\":\"mailto:alternate@example.com\"}")
            + "&stateId="
            + UUID.randomUUID();
    var body = "{\"text\":\"café + 日本語 & = %\"}";
    mvc.perform(
            alternate(
                endpoint,
                "PUT",
                parameters + "&Content-Type=application%2Fjson&content=" + encode(body)))
        .andExpect(status().isNoContent());
    var etag = StateService.etag(body.getBytes(StandardCharsets.UTF_8));
    mvc.perform(alternate(endpoint, "GET", parameters))
        .andExpect(status().isOk())
        .andExpect(content().bytes(body.getBytes(StandardCharsets.UTF_8)))
        .andExpect(content().contentType("application/json"));
    mvc.perform(
            alternate(endpoint, "PUT", parameters + "&If-Match=%22wrong%22&content=replacement"))
        .andExpect(status().isPreconditionFailed());
    mvc.perform(
            alternate(
                endpoint,
                "POST",
                parameters
                    + "&Content-Type=application%2Fjson&If-Match="
                    + encode(etag)
                    + "&content="
                    + encode("{\"updated\":true}")))
        .andExpect(status().isNoContent());
    mvc.perform(alternate(endpoint, "GET", parameters))
        .andExpect(jsonPath("$.text").value("café + 日本語 & = %"))
        .andExpect(jsonPath("$.updated").value(true));
    mvc.perform(alternate(endpoint, "DELETE", parameters)).andExpect(status().isNoContent());
    mvc.perform(alternate(endpoint, "GET", parameters)).andExpect(status().isNotFound());
  }

  @Test
  void invalidRequestsKeepXapiResponseHeadersAndNormalBodyValidation() throws Exception {
    mvc.perform(alternate("/xapi/statements", "PUT", "statementId=" + UUID.randomUUID()))
        .andExpect(status().isBadRequest());
    mvc.perform(
            alternate(
                "/xapi/statements", "PUT", "statementId=" + UUID.randomUUID() + "&content=%7B%7D"))
        .andExpect(status().isBadRequest());
    mvc.perform(post("/xapi/statements?method=GET&limit=1"))
        .andExpect(status().isBadRequest())
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().exists("X-Experience-API-Consistent-Through"));
  }

  private static MockHttpServletRequestBuilder alternate(String path, String method, String body) {
    return post(path + "?method=" + method)
        .contentType("application/x-www-form-urlencoded")
        .content(body.getBytes(StandardCharsets.UTF_8));
  }

  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
