/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:response-headers;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class ResponseHeadersIntegrationTest {

  private static final String STATEMENT =
      """
      {"actor":{"mbox":"mailto:headers@example.com"},
       "verb":{"id":"http://adlnet.gov/expapi/verbs/completed"},
       "object":{"id":"https://example.com/header-test"}}
      """;

  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper mapper;

  @Test
  void putAndPostCommitBeforeLaterGetConsistencyBoundary() throws Exception {
    final var putId = UUID.randomUUID();
    mvc.perform(
            put("/xapi/statements")
                .param("statementId", putId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(STATEMENT))
        .andExpect(status().isNoContent())
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"));
    assertRetrievableThroughBoundary(putId);

    final var postResult =
        mvc.perform(
                post("/xapi/statements")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[" + STATEMENT + "]"))
            .andExpect(status().isOk())
            .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
            .andReturn();
    final var postId =
        UUID.fromString(
            mapper.readTree(postResult.getResponse().getContentAsString()).get(0).asText());
    assertRetrievableThroughBoundary(postId);
  }

  @Test
  void validationAndNotFoundResponsesIncludeBothHeaders() throws Exception {
    mvc.perform(
            put("/xapi/statements")
                .param("statementId", "invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(STATEMENT))
        .andExpect(status().isBadRequest())
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().exists("X-Experience-API-Consistent-Through"));
    mvc.perform(get("/xapi/statements").param("statementId", UUID.randomUUID().toString()))
        .andExpect(status().isNotFound())
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().exists("X-Experience-API-Consistent-Through"));
  }

  @Test
  void stateErrorsIncludeOnlyTheVersionHeader() throws Exception {
    mvc.perform(get("/xapi/activities/state"))
        .andExpect(status().isBadRequest())
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().doesNotExist("X-Experience-API-Consistent-Through"));
  }

  private void assertRetrievableThroughBoundary(UUID id) throws Exception {
    final var response =
        mvc.perform(get("/xapi/statements").param("statementId", id.toString()))
            .andExpect(status().isOk())
            .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
            .andReturn()
            .getResponse();
    final var stored =
        Instant.parse(mapper.readTree(response.getContentAsString()).get("stored").asText());
    final var boundary = Instant.parse(response.getHeader("X-Experience-API-Consistent-Through"));
    assertFalse(stored.isAfter(boundary));
  }
}
