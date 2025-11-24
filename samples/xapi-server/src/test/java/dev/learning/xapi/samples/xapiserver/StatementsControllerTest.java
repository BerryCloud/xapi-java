/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
@WebMvcTest(
    value = {StatementController.class},
    properties = "spring.jackson.deserialization.ACCEPT_SINGLE_VALUE_AS_ARRAY = true")
class StatementControllerTest {

  @Autowired private MockMvc mvc;

  @MockitoBean private StatementService statementService;

  @Test
  void whenPuttingStatementThenStatusIsNoContent() throws Exception {

    // When Putting Statement
    mvc.perform(
            put("/xapi/statements")
                .content(
                    "{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}}}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("statementId", "04417d92-2d51-4789-92b0-62b0a1b0127b"))

        // Then Status Is No Content
        .andExpect(status().isNoContent());
  }

  @Test
  void whenPostingStatementThenStatusIsOk() throws Exception {

    // When Posting Statement
    mvc.perform(
            post("/xapi/statements")
                .content(
                    "{\"id\":\"676d2f88-7cf2-4aac-857f-c307a6a74c5b\",\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}}}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))

        // Then Status Is Ok
        .andExpect(status().isOk());
  }

  @Test
  void whenPostingMultipleStatementsThenStatusIsOk() throws Exception {

    // When Posting Multiple Statements
    mvc.perform(
            post("/xapi/statements")
                .content(
                    "[{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}}}, {\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}}}]")
                .contentType(MediaType.APPLICATION_JSON_VALUE))

        // Then Status Is Ok
        .andExpect(status().isOk());
  }

  @Test
  void whenGettingMultipleStatementsWithSinceParameterThenStatusIsNotImplemented()
      throws Exception {

    // When Getting Multiple Statements With Since Parameter
    mvc.perform(get("/xapi/statements?since=2017-03-01T12:30:00.000+00"))

        // Then Status Is Not Implemented
        .andExpect(status().isNotImplemented());
  }

  @Test
  void whenGettingMultipleStatementsWithNegativeTimezoneOffsetThenStatusIsBadRequest()
      throws Exception {

    // When Getting Multiple Statements With Negative Timezone Offset
    mvc.perform(get("/xapi/statements?since=2017-03-01T12:30:00.000-00"))

        // Then Status Is Bad Request
        .andExpect(status().isBadRequest());
  }
}
