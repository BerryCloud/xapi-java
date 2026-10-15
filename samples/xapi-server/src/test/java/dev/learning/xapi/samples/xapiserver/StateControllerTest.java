/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/** State Resource integration tests using the sample's H2 persistence. */
@SpringBootTest
@AutoConfigureMockMvc
class StateControllerTest {

  private static final String ENDPOINT = "/xapi/activities/state";
  private static final String ACTIVITY = "https://example.com/activity";
  private static final String AGENT = "{\"mbox\":\"mailto:learner@example.com\"}";
  private static final String REGISTRATION = "8c902759-7f8d-4b24-8649-318ea3ecf139";

  @Autowired private MockMvc mvc;
  @Autowired private StateRepository repository;

  @BeforeEach
  void clearStates() {
    repository.deleteAll();
  }

  @Test
  void putPreservesBinaryContentsAndMediaType() throws Exception {
    byte[] bytes = {0, 1, -1, 42};
    String contentType = "application/octet-stream;charset=UTF-8";
    mvc.perform(
            context(put(ENDPOINT))
                .param("stateId", "progress")
                .contentType(contentType)
                .content(bytes))
        .andExpect(status().isNoContent());

    mvc.perform(document(get(ENDPOINT)))
        .andExpect(status().isOk())
        .andExpect(content().bytes(bytes))
        .andExpect(content().contentType(contentType))
        .andExpect(header().string(HttpHeaders.ETAG, StateService.etag(bytes)))
        .andExpect(header().exists(HttpHeaders.LAST_MODIFIED));
  }

  @Test
  void putReplacesAnExistingDocumentWithoutRequiringPreconditions() throws Exception {
    putJson("{\"old\":true}");
    putJson("{\"new\":true}");
    mvc.perform(document(get(ENDPOINT)))
        .andExpect(jsonPath("$.new", is(true)))
        .andExpect(jsonPath("$.old").doesNotExist());
    assertEquals(1, repository.count());
  }

  @Test
  void postCreatesThenMergesOnlyTopLevelProperties() throws Exception {
    mvc.perform(
            document(post(ENDPOINT))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"keep\":1,\"nested\":{\"old\":true},\"change\":1}"))
        .andExpect(status().isNoContent());
    mvc.perform(
            document(post(ENDPOINT))
                .contentType("application/json;charset=UTF-8")
                .content("{\"nested\":{\"new\":true},\"change\":2,\"added\":null}"))
        .andExpect(status().isNoContent());
    mvc.perform(document(get(ENDPOINT)))
        .andExpect(jsonPath("$.keep", is(1)))
        .andExpect(jsonPath("$.change", is(2)))
        .andExpect(jsonPath("$.nested.new", is(true)))
        .andExpect(jsonPath("$.nested.old").doesNotExist())
        .andExpect(
            content().json("{\"keep\":1,\"nested\":{\"new\":true},\"change\":2,\"added\":null}"));
  }

  @Test
  void invalidPostDoesNotChangeTheExistingDocument() throws Exception {
    String original = "{\"keep\":true}";
    putJson(original);
    for (String invalid : new String[] {"[]", "null", "42", "{", "{} {}"}) {
      mvc.perform(document(post(ENDPOINT)).contentType(MediaType.APPLICATION_JSON).content(invalid))
          .andExpect(status().isBadRequest());
    }
    mvc.perform(document(post(ENDPOINT)).contentType(MediaType.TEXT_PLAIN).content("{}"))
        .andExpect(status().isBadRequest());
    mvc.perform(document(get(ENDPOINT))).andExpect(content().string(original));
  }

  @Test
  void postCannotMergeIntoANonJsonDocument() throws Exception {
    mvc.perform(document(put(ENDPOINT)).contentType(MediaType.TEXT_PLAIN).content("original"))
        .andExpect(status().isNoContent());
    mvc.perform(document(post(ENDPOINT)).contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
    mvc.perform(document(get(ENDPOINT))).andExpect(content().string("original"));
  }

  @Test
  void putAllowsEmptyDocumentsAndDefaultsToBinaryContentType() throws Exception {
    mvc.perform(document(put(ENDPOINT))).andExpect(status().isNoContent());
    mvc.perform(document(get(ENDPOINT)))
        .andExpect(status().isOk())
        .andExpect(content().bytes(new byte[0]))
        .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
  }

  @Test
  void missingStateReturnsNotFoundAndDeleteIsIdempotent() throws Exception {
    mvc.perform(document(get(ENDPOINT))).andExpect(status().isNotFound());
    mvc.perform(document(delete(ENDPOINT))).andExpect(status().isNoContent());
    putJson("{}");
    mvc.perform(document(delete(ENDPOINT))).andExpect(status().isNoContent());
    mvc.perform(document(get(ENDPOINT))).andExpect(status().isNotFound());
  }

  @Test
  void agentNamesObjectTypeAndPropertyOrderDoNotChangeIdentity() throws Exception {
    putJson("{\"saved\":true}");
    mvc.perform(
            get(ENDPOINT)
                .param("activityId", ACTIVITY)
                .param("stateId", "progress")
                .param(
                    "agent",
                    "{\"name\":\"Renamed"
                        + " learner\",\"objectType\":\"Agent\",\"mbox\":\"mailto:learner@example.com\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.saved", is(true)));
  }

  @Test
  void accountIdentityDoesNotDependOnPropertyOrder() throws Exception {
    String first = "{\"account\":{\"homePage\":\"https://example.com\",\"name\":\"user\"}}";
    String second =
        "{\"name\":\"Display"
            + " name\",\"account\":{\"name\":\"user\",\"homePage\":\"https://example.com\"}}";
    mvc.perform(
            put(ENDPOINT)
                .param("activityId", ACTIVITY)
                .param("agent", first)
                .param("stateId", "progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isNoContent());
    mvc.perform(
            get(ENDPOINT)
                .param("activityId", ACTIVITY)
                .param("agent", second)
                .param("stateId", "progress"))
        .andExpect(status().isOk());
  }

  @Test
  void stateDocumentsAreIsolatedByActivityAgentAndRegistration() throws Exception {
    putJson("{\"unregistered\":true}");
    mvc.perform(
            document(put(ENDPOINT))
                .param("registration", REGISTRATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"registered\":true}"))
        .andExpect(status().isNoContent());
    mvc.perform(document(get(ENDPOINT))).andExpect(jsonPath("$.unregistered", is(true)));
    mvc.perform(document(get(ENDPOINT)).param("registration", REGISTRATION))
        .andExpect(jsonPath("$.registered", is(true)));
    mvc.perform(
            get(ENDPOINT)
                .param("activityId", "https://example.com/other")
                .param("agent", AGENT)
                .param("stateId", "progress"))
        .andExpect(status().isNotFound());
    mvc.perform(
            get(ENDPOINT)
                .param("activityId", ACTIVITY)
                .param("agent", "{\"mbox\":\"mailto:other@example.com\"}")
                .param("stateId", "progress"))
        .andExpect(status().isNotFound());
  }

  @Test
  void listAndBulkDeleteRespectRegistrationFilters() throws Exception {
    putJson("{}");
    mvc.perform(
            context(put(ENDPOINT))
                .param("stateId", "registered")
                .param("registration", REGISTRATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isNoContent());
    mvc.perform(context(get(ENDPOINT)))
        .andExpect(content().json("[\"progress\"]"))
        .andExpect(header().exists(HttpHeaders.ETAG))
        .andExpect(header().exists(HttpHeaders.LAST_MODIFIED));
    mvc.perform(context(get(ENDPOINT)).param("registration", REGISTRATION))
        .andExpect(content().json("[\"registered\"]"));
    mvc.perform(context(delete(ENDPOINT))).andExpect(status().isNoContent());
    mvc.perform(context(get(ENDPOINT))).andExpect(content().json("[]"));
    mvc.perform(context(get(ENDPOINT)).param("registration", REGISTRATION))
        .andExpect(content().json("[\"registered\"]"));
    mvc.perform(context(delete(ENDPOINT)).param("registration", StateKey.DEFAULT_REGISTRATION))
        .andExpect(status().isNoContent());
    mvc.perform(context(get(ENDPOINT)).param("registration", REGISTRATION))
        .andExpect(content().json("[\"registered\"]"));
    mvc.perform(context(delete(ENDPOINT)).param("registration", REGISTRATION))
        .andExpect(status().isNoContent());
    mvc.perform(context(get(ENDPOINT)).param("registration", REGISTRATION))
        .andExpect(content().json("[]"));
  }

  @Test
  void omittedEmptyAndNilRegistrationsAddressTheSameDocument() throws Exception {
    mvc.perform(
            document(put(ENDPOINT))
                .param("registration", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"original\":true}"))
        .andExpect(status().isNoContent());
    assertEquals(
        StateKey.NIL_REGISTRATION, repository.findAll().getFirst().getId().getRegistration());
    mvc.perform(document(get(ENDPOINT))).andExpect(content().json("{\"original\":true}"));
    mvc.perform(document(get(ENDPOINT)).param("registration", StateKey.DEFAULT_REGISTRATION))
        .andExpect(content().json("{\"original\":true}"));
    mvc.perform(
            document(post(ENDPOINT))
                .param("registration", StateKey.DEFAULT_REGISTRATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"merged\":true}"))
        .andExpect(status().isNoContent());
    mvc.perform(document(get(ENDPOINT)).param("registration", ""))
        .andExpect(content().json("{\"original\":true,\"merged\":true}"));
    mvc.perform(
            document(put(ENDPOINT))
                .param("registration", StateKey.DEFAULT_REGISTRATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"replaced\":true}"))
        .andExpect(status().isNoContent());
    assertEquals(1, repository.count());
    mvc.perform(context(get(ENDPOINT)).param("registration", StateKey.DEFAULT_REGISTRATION))
        .andExpect(content().json("[\"progress\"]"));
    mvc.perform(document(delete(ENDPOINT)).param("registration", StateKey.DEFAULT_REGISTRATION))
        .andExpect(status().isNoContent());
    mvc.perform(document(get(ENDPOINT))).andExpect(status().isNotFound());
  }

  @Test
  void compositeKeyPreservesLongDistinctStateIds() throws Exception {
    String prefix = "state".repeat(256);
    for (String suffix : new String[] {"first", "second"}) {
      mvc.perform(
              context(put(ENDPOINT))
                  .param("stateId", prefix + suffix)
                  .contentType(MediaType.TEXT_PLAIN)
                  .content(suffix))
          .andExpect(status().isNoContent());
    }
    assertEquals(2, repository.count());
    for (String suffix : new String[] {"first", "second"}) {
      mvc.perform(context(get(ENDPOINT)).param("stateId", prefix + suffix))
          .andExpect(status().isOk())
          .andExpect(content().string(suffix));
    }
    mvc.perform(context(get(ENDPOINT))).andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void listingDoesNotLoadDocumentContents() throws Exception {
    byte[] contents = new byte[1024 * 1024];
    mvc.perform(
            document(put(ENDPOINT))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(contents))
        .andExpect(status().isNoContent());

    var selects =
        sqlFor(
                context(get(ENDPOINT)),
                status().isOk(),
                content().json("[\"progress\"]"),
                header().exists(HttpHeaders.ETAG),
                header().exists(HttpHeaders.LAST_MODIFIED))
            .stream()
            .filter(sql -> sql.contains("select"))
            .toList();
    assertFalse(selects.isEmpty(), "Listing must execute a database query");
    assertTrue(
        selects.stream().noneMatch(sql -> sql.contains("contents")),
        "Listing must not select document contents or load full entities");
  }

  @Test
  void bulkDeleteUsesOneStatementWithoutReadingDocuments() throws Exception {
    byte[] contents = new byte[1024 * 1024];
    for (String stateId : new String[] {"first", "second"}) {
      mvc.perform(
              context(put(ENDPOINT))
                  .param("stateId", stateId)
                  .param("registration", REGISTRATION)
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .content(contents))
          .andExpect(status().isNoContent());
    }
    putJson("{}");

    var registeredDelete =
        sqlFor(
            context(delete(ENDPOINT)).param("registration", REGISTRATION), status().isNoContent());
    assertEquals(1, registeredDelete.size(), "Bulk deletion must use a single database statement");
    assertTrue(registeredDelete.getFirst().contains("delete"));
    assertFalse(registeredDelete.getFirst().contains("select"));
    mvc.perform(context(get(ENDPOINT))).andExpect(content().json("[\"progress\"]"));

    var unregisteredDelete = sqlFor(context(delete(ENDPOINT)), status().isNoContent());
    assertEquals(1, unregisteredDelete.size());
    assertTrue(unregisteredDelete.getFirst().contains("delete"));
    assertFalse(unregisteredDelete.getFirst().contains("select"));
    mvc.perform(context(get(ENDPOINT))).andExpect(content().json("[]"));
  }

  @Test
  void bulkDeleteRejectsPreconditionsWithoutTouchingTheDatabase() throws Exception {
    putJson("{}");
    for (String header : new String[] {HttpHeaders.IF_MATCH, HttpHeaders.IF_NONE_MATCH}) {
      var statements =
          sqlFor(context(delete(ENDPOINT)).header(header, "*"), status().isBadRequest());
      assertTrue(statements.isEmpty(), "Invalid bulk deletion must not access stored documents");
    }
    mvc.perform(document(get(ENDPOINT))).andExpect(content().json("{}"));
  }

  @Test
  void bulkDeleteDoesNotAffectOtherContexts() throws Exception {
    putJson("{}");
    mvc.perform(
            put(ENDPOINT)
                .param("activityId", "https://example.com/other")
                .param("agent", AGENT)
                .param("stateId", "progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isNoContent());
    mvc.perform(
            put(ENDPOINT)
                .param("activityId", ACTIVITY)
                .param("agent", "{\"mbox\":\"mailto:other@example.com\"}")
                .param("stateId", "progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isNoContent());
    mvc.perform(context(delete(ENDPOINT))).andExpect(status().isNoContent());
    assertEquals(2, repository.count());
  }

  @Test
  void sinceFilterIsExclusive() throws Exception {
    putJson("{}");
    Instant updated = Instant.parse("2026-01-01T12:00:00Z");
    var state = repository.findAll().getFirst();
    state.setUpdated(updated);
    repository.saveAndFlush(state);
    mvc.perform(context(get(ENDPOINT)).param("since", updated.toString()))
        .andExpect(jsonPath("$", hasSize(0)));
    mvc.perform(context(get(ENDPOINT)).param("since", updated.minusNanos(1).toString()))
        .andExpect(content().json("[\"progress\"]"));
  }

  @Test
  void conditionalWritesRejectStaleTagsAndPreserveTheDocument() throws Exception {
    putJson("{\"original\":true}");
    for (var request :
        new MockHttpServletRequestBuilder[] {put(ENDPOINT), post(ENDPOINT), delete(ENDPOINT)}) {
      mvc.perform(
              document(request)
                  .header(HttpHeaders.IF_MATCH, "\"stale\"")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{}"))
          .andExpect(status().isPreconditionFailed());
    }
    mvc.perform(
            document(put(ENDPOINT))
                .header(HttpHeaders.IF_NONE_MATCH, "*")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isPreconditionFailed());
    mvc.perform(document(get(ENDPOINT))).andExpect(jsonPath("$.original", is(true)));
  }

  @Test
  void conditionalCreateUpdateAndDeleteAcceptCurrentTags() throws Exception {
    mvc.perform(
            document(put(ENDPOINT))
                .header(HttpHeaders.IF_NONE_MATCH, "*")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isNoContent());
    String tag =
        mvc.perform(document(get(ENDPOINT))).andReturn().getResponse().getHeader(HttpHeaders.ETAG);
    mvc.perform(
            document(put(ENDPOINT))
                .header(HttpHeaders.IF_MATCH, "\"other\", " + tag)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"updated\":true}"))
        .andExpect(status().isNoContent());
    String newTag =
        mvc.perform(document(get(ENDPOINT))).andReturn().getResponse().getHeader(HttpHeaders.ETAG);
    mvc.perform(document(delete(ENDPOINT)).header(HttpHeaders.IF_MATCH, newTag))
        .andExpect(status().isNoContent());
    mvc.perform(
            document(put(ENDPOINT))
                .header(HttpHeaders.IF_MATCH, "*")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isPreconditionFailed());
  }

  @Test
  void invalidContextsAndMissingParametersReturnBadRequest() throws Exception {
    mvc.perform(get(ENDPOINT).param("agent", AGENT)).andExpect(status().isBadRequest());
    mvc.perform(get(ENDPOINT).param("activityId", ACTIVITY)).andExpect(status().isBadRequest());
    mvc.perform(context(put(ENDPOINT)).content("{}")).andExpect(status().isBadRequest());
    mvc.perform(get(ENDPOINT).param("activityId", "relative").param("agent", AGENT))
        .andExpect(status().isBadRequest());
    for (String agent :
        new String[] {
          "{",
          "null",
          "{}",
          "{\"name\":\"No identifier\"}",
          "{\"mbox\":\"invalid\"}",
          "{\"mbox\":\"mailto:a@example.com\",\"openid\":\"https://example.com\"}"
        }) {
      mvc.perform(get(ENDPOINT).param("activityId", ACTIVITY).param("agent", agent))
          .andExpect(status().isBadRequest());
    }
    mvc.perform(context(get(ENDPOINT)).param("registration", "not-a-uuid"))
        .andExpect(status().isBadRequest());
    mvc.perform(context(get(ENDPOINT)).param("since", "not-a-timestamp"))
        .andExpect(status().isBadRequest());
    mvc.perform(document(get(ENDPOINT)).param("since", Instant.now().toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void allStateMethodsRejectInvalidAgentParametersWithoutChangingDocuments() throws Exception {
    putJson("{\"original\":true}");
    for (String agent :
        new String[] {
          "{",
          "null",
          "[]",
          "{\"objectType\":\"Group\",\"mbox\":\"mailto:a@example.com\"}",
          "{}",
          "{\"mbox\":\"invalid\"}",
          "{\"mbox\":\"mailto:a@example.com\",\"openid\":\"https://example.com\"}"
        }) {
      for (var request :
          new MockHttpServletRequestBuilder[] {
            get(ENDPOINT), put(ENDPOINT), post(ENDPOINT), delete(ENDPOINT)
          }) {
        mvc.perform(
                request
                    .param("activityId", ACTIVITY)
                    .param("agent", agent)
                    .param("stateId", "progress")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
            .andExpect(status().isBadRequest());
      }
    }
    mvc.perform(document(get(ENDPOINT))).andExpect(content().json("{\"original\":true}"));
  }

  @Test
  void allStateMethodsRejectInvalidRegistrationAndActivityIds() throws Exception {
    putJson("{\"original\":true}");
    for (String method : new String[] {"GET", "PUT", "POST", "DELETE"}) {
      for (String registration :
          new String[] {
            "not-a-uuid",
            "00000000-0000-0000-0000-000000000001",
            "4c30eeb1-15ac-4833-cb0d-88613ba65267"
          }) {
        mvc.perform(
                document(request(HttpMethod.valueOf(method), ENDPOINT))
                    .param("registration", registration)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
            .andExpect(status().isBadRequest());
      }
      mvc.perform(
              request(HttpMethod.valueOf(method), ENDPOINT)
                  .param("activityId", "relative")
                  .param("agent", AGENT)
                  .param("stateId", "progress")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{}"))
          .andExpect(status().isBadRequest());
    }
    mvc.perform(document(get(ENDPOINT))).andExpect(content().json("{\"original\":true}"));
  }

  @Test
  void statementStorageStillWorksAlongsideStateStorage() throws Exception {
    String id = UUID.randomUUID().toString();
    mvc.perform(
            put("/xapi/statements")
                .param("statementId", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "actor": {"mbox": "mailto:learner@example.com"},
                      "verb": {"id": "http://adlnet.gov/expapi/verbs/experienced"},
                      "object": {"id": "https://example.com/activity"}
                    }
                    """))
        .andExpect(status().isNoContent());
    mvc.perform(get("/xapi/statements").param("statementId", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(id)))
        .andExpect(jsonPath("$.actor.mbox", is("mailto:learner@example.com")));
  }

  private List<String> sqlFor(MockHttpServletRequestBuilder request, ResultMatcher... expectations)
      throws Exception {
    Logger sqlLogger = (Logger) LoggerFactory.getLogger("org.hibernate.SQL");
    Level previousLevel = sqlLogger.getLevel();
    ListAppender<ILoggingEvent> statements = new ListAppender<>();
    statements.start();
    sqlLogger.addAppender(statements);
    sqlLogger.setLevel(Level.DEBUG);
    try {
      mvc.perform(request).andExpectAll(expectations);
    } finally {
      sqlLogger.detachAppender(statements);
      sqlLogger.setLevel(previousLevel);
      statements.stop();
    }
    return statements.list.stream()
        .map(event -> event.getFormattedMessage().toLowerCase(Locale.ROOT))
        .toList();
  }

  private MockHttpServletRequestBuilder context(MockHttpServletRequestBuilder request) {
    return request.param("activityId", ACTIVITY).param("agent", AGENT);
  }

  private MockHttpServletRequestBuilder document(MockHttpServletRequestBuilder request) {
    return context(request).param("stateId", "progress");
  }

  private void putJson(String json) throws Exception {
    mvc.perform(document(put(ENDPOINT)).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNoContent());
  }
}
