/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * State Controller Test.
 *
 * @author Thomas Turrell-Croft
 */
@WebMvcTest(value = {StateController.class})
class StateControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private StateService stateService;

  @Test
  void whenPuttingStateThenStatusIsNoContent() throws Exception {

    // When Putting State
    mvc.perform(put("/xapi/activities/state")
        .content("{\"message\":\"Hello World!\"}")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .param("activityId", "https://example.com/activity/1")
        .param("agent", "{\"objectType\":\"Agent\",\"mbox\":\"mailto:another@example.com\"}")
        .param("stateId", "bookmark"))

        // Then Status Is No Content
        .andExpect(status().isNoContent());
  }

  @Test
  void whenPostingStateThenStatusIsNoContent() throws Exception {

    // When Posting State
    mvc.perform(post("/xapi/activities/state")
        .content("{\"message\":\"Hello World!\"}")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .param("activityId", "https://example.com/activity/1")
        .param("agent", "{\"objectType\":\"Agent\",\"mbox\":\"mailto:another@example.com\"}")
        .param("stateId", "bookmark"))

        // Then Status Is No Content
        .andExpect(status().isNoContent());
  }

  @Test
  void whenGettingStateThenStatusIsNotFound() throws Exception {

    // When Getting State
    mvc.perform(get("/xapi/activities/state")
        .param("activityId", "https://example.com/activity/1")
        .param("agent", "{\"objectType\":\"Agent\",\"mbox\":\"mailto:another@example.com\"}")
        .param("stateId", "bookmark"))

        // Then Status Is Not Found
        .andExpect(status().isNotFound());
  }

  @Test
  void whenGettingStateIdsThenStatusIsOk() throws Exception {

    // When Getting State IDs
    mvc.perform(get("/xapi/activities/state")
        .param("activityId", "https://example.com/activity/1")
        .param("agent", "{\"objectType\":\"Agent\",\"mbox\":\"mailto:another@example.com\"}"))

        // Then Status Is Ok
        .andExpect(status().isOk());
  }

  @Test
  void whenDeletingStateThenStatusIsNoContent() throws Exception {

    // When Deleting State
    mvc.perform(delete("/xapi/activities/state")
        .param("activityId", "https://example.com/activity/1")
        .param("agent", "{\"objectType\":\"Agent\",\"mbox\":\"mailto:another@example.com\"}")
        .param("stateId", "bookmark"))

        // Then Status Is No Content
        .andExpect(status().isNoContent());
  }

  @Test
  void whenDeletingStatesThenStatusIsNoContent() throws Exception {

    // When Deleting States
    mvc.perform(delete("/xapi/activities/state")
        .param("activityId", "https://example.com/activity/1")
        .param("agent", "{\"objectType\":\"Agent\",\"mbox\":\"mailto:another@example.com\"}"))

        // Then Status Is No Content
        .andExpect(status().isNoContent());
  }

}
