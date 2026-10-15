/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Statement Controller Test.
 *
 * @author Martin Myslik
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@Import({StatementConsistency.class, WebMvcConfiguration.class})
@WebMvcTest(
    value = {StatementController.class},
    properties = "spring.jackson.deserialization.ACCEPT_SINGLE_VALUE_AS_ARRAY = true")
class StatementControllerTest {

  private static final String VALID_STATEMENT =
      """
      {
        "actor": {"mbox": "mailto:another@example.com"},
        "verb": {"id": "https://example.com/verb"},
        "object": {"id": "https://example.com/activity"}
      }
      """;

  @Autowired private MockMvc mvc;

  @MockitoBean private StatementService statementService;

  @Test
  void whenPuttingStatementThenStatusIsNoContent() throws Exception {

    // When Putting Statement
    mvc.perform(
            put("/xapi/statements")
                .content(
                    "{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N"
                        + " Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple"
                        + " Statement\"}}}}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("statementId", "04417d92-2d51-4789-92b0-62b0a1b0127b"))

        // Then Status Is No Content
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().string("X-Experience-API-Consistent-Through", notNullValue()))
        .andExpect(status().isNoContent());
  }

  @Test
  void whenPostingStatementThenStatusIsOk() throws Exception {

    // When Posting Statement
    mvc.perform(
            post("/xapi/statements")
                .content(
                    "{\"id\":\"676d2f88-7cf2-4aac-857f-c307a6a74c5b\",\"actor\":{\"objectType\":\"Agent\",\"name\":\"A"
                        + " N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple"
                        + " Statement\"}}}}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))

        // Then Status Is Ok
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().string("X-Experience-API-Consistent-Through", notNullValue()))
        .andExpect(status().isOk());
  }

  @Test
  void whenPostingMultipleStatementsThenStatusIsOk() throws Exception {

    // When Posting Multiple Statements
    mvc.perform(
            post("/xapi/statements")
                .content(
                    "[{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N"
                        + " Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple"
                        + " Statement\"}}}}, {\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N"
                        + " Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple"
                        + " Statement\"}}}}]")
                .contentType(MediaType.APPLICATION_JSON_VALUE))

        // Then Status Is Ok
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().string("X-Experience-API-Consistent-Through", notNullValue()))
        .andExpect(status().isOk());
  }

  @Test
  void whenGettingMultipleStatementsWithSinceParameterThenStatusIsNotImplemented()
      throws Exception {

    // When Getting Multiple Statements With Since Parameter
    mvc.perform(get("/xapi/statements?since=2017-03-01T12:30:00.000+00"))

        // Then Status Is Not Implemented
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().string("X-Experience-API-Consistent-Through", notNullValue()))
        .andExpect(status().isNotImplemented());
  }

  @Test
  void whenGettingMultipleStatementsWithNegativeTimezoneOffsetThenStatusIsBadRequest()
      throws Exception {

    // When Getting Multiple Statements With Negative Timezone Offset
    mvc.perform(get("/xapi/statements?since=2017-03-01T12:30:00.000-00"))

        // Then Status Is Bad Request
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().string("X-Experience-API-Consistent-Through", notNullValue()))
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "unknown", "StatementId", "VoidedStatementId", "Agent", "Verb", "Activity",
        "Registration", "Related_activities", "Related_agents", "Since", "Until", "Limit",
        "Format", "Attachments", "Ascending"
      })
  void rejectsUnknownOrIncorrectlyCapitalisedParameters(String parameter) throws Exception {
    mvc.perform(get("/xapi/statements").param(parameter, "value"))
        .andExpect(status().isBadRequest())
        .andExpect(header().string("X-Experience-API-Version", "1.0.3"))
        .andExpect(header().string("X-Experience-API-Consistent-Through", notNullValue()));
    verifyNoInteractions(statementService);
  }

  static Stream<Arguments> incompatibleParameters() {
    return Stream.of("statementId", "voidedStatementId")
        .flatMap(
            id ->
                Stream.of(
                        "agent",
                        "verb",
                        "activity",
                        "registration",
                        "related_activities",
                        "related_agents",
                        "since",
                        "until",
                        "limit",
                        "ascending")
                    .map(filter -> Arguments.of(id, filter)));
  }

  @ParameterizedTest
  @MethodSource("incompatibleParameters")
  void rejectsFiltersAlongsideSingleStatementIds(String id, String filter) throws Exception {
    mvc.perform(
            get("/xapi/statements")
                .param(id, "04417d92-2d51-4789-92b0-62b0a1b0127b")
                .param(filter, "value"))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(statementService);
  }

  @Test
  void rejectsBothSingleStatementIds() throws Exception {
    mvc.perform(
            get("/xapi/statements")
                .param("statementId", "04417d92-2d51-4789-92b0-62b0a1b0127b")
                .param("voidedStatementId", "676d2f88-7cf2-4aac-857f-c307a6a74c5b"))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(statementService);
  }

  @ParameterizedTest
  @CsvSource({"format,exact", "attachments,false"})
  void permitsRepresentationParametersWithStatementId(String parameter, String value)
      throws Exception {
    final var id = UUID.fromString("04417d92-2d51-4789-92b0-62b0a1b0127b");
    when(statementService.getStatement(id)).thenReturn(Optional.empty());
    mvc.perform(get("/xapi/statements").param("statementId", id.toString()).param(parameter, value))
        .andExpect(status().isNotFound());
    verify(statementService).getStatement(id);
  }

  @Test
  void validVoidedStatementRequestDoesNotFallThroughToStatementList() throws Exception {
    mvc.perform(
            get("/xapi/statements")
                .param("voidedStatementId", "04417d92-2d51-4789-92b0-62b0a1b0127b")
                .param("format", "exact")
                .param("attachments", "false"))
        .andExpect(status().isNotImplemented());
    verifyNoInteractions(statementService);
  }

  @Test
  void permitsRecognisedListParameters() throws Exception {
    mvc.perform(
            get("/xapi/statements")
                .param("agent", "{\"mbox\":\"mailto:another@example.com\"}")
                .param("verb", "https://example.com/verb")
                .param("activity", "https://example.com/activity")
                .param("registration", "04417d92-2d51-4789-92b0-62b0a1b0127b")
                .param("related_activities", "false")
                .param("related_agents", "false")
                .param("until", "2026-10-15T18:00:00Z")
                .param("limit", "10")
                .param("format", "exact")
                .param("attachments", "false")
                .param("ascending", "true"))
        .andExpect(status().isOk());
    verify(statementService).getStatements();
  }

  @Test
  void rejectsInvalidHeadRequest() throws Exception {
    mvc.perform(head("/xapi/statements").param("unknown", "value"))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(statementService);
  }

  @Test
  void rejectsUnexpectedPutParameterBeforeStoringStatement() throws Exception {
    mvc.perform(
            put("/xapi/statements")
                .param("statementId", "04417d92-2d51-4789-92b0-62b0a1b0127b")
                .param("unknown", "value")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_STATEMENT))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(statementService);
  }

  @Test
  void rejectsUnexpectedPostParameterBeforeStoringStatements() throws Exception {
    mvc.perform(
            post("/xapi/statements")
                .param("unknown", "value")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_STATEMENT))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(statementService);
  }
}
