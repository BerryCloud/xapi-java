/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import dev.learning.xapi.model.Agent;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic implementation of xAPI state GET, PUT, POST and DELETE resources.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#23-state-resource">xAPI
 *      State Resource</a>
 *
 * @author Thomas Turrell-Croft
 */
@Validated
@RestController
@RequestMapping(value = "/xapi/activities/state")
public class StateController {

  Logger log = LoggerFactory.getLogger(StateController.class);

  private final StateService stateService;

  public StateController(StateService stateService) {
    this.stateService = stateService;
  }

  /**
   * Get a single State document.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the optional registration
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
   *      Document</a>
   */
  @GetMapping(params = {"activityId", "agent", "stateId"})
  public ResponseEntity<String> getState(@RequestParam(required = true) String activityId,
      @Valid @RequestParam(required = true) Agent agent,
      @RequestParam(required = true) String stateId,
      @RequestParam(required = false) UUID registration) {

    log.debug("GET state");

    final var stateEntity = stateService.getState(activityId, agent, stateId, registration);

    return stateEntity
        .map(entity -> ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(
                entity.getContentType() != null ? entity.getContentType()
                    : MediaType.APPLICATION_JSON_VALUE))
            .body(entity.getStateDocument()))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Get all stateIds for an activity and agent.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param registration the optional registration
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-get">Multiple
   *      Document GET</a>
   */
  @GetMapping(params = {"activityId", "agent", "!stateId"})
  public ResponseEntity<List<String>> getStateIds(
      @RequestParam(required = true) String activityId,
      @Valid @RequestParam(required = true) Agent agent,
      @RequestParam(required = false) UUID registration) {

    log.debug("GET state ids");

    final var stateIds = stateService.getStateIds(activityId, agent, registration);

    return ResponseEntity.ok(stateIds);
  }

  /**
   * Put a State document.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the optional registration
   * @param stateDocument the state document
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
   *      Document</a>
   */
  @PutMapping(params = {"activityId", "agent", "stateId"})
  public ResponseEntity<Void> putState(@RequestParam(required = true) String activityId,
      @Valid @RequestParam(required = true) Agent agent,
      @RequestParam(required = true) String stateId,
      @RequestParam(required = false) UUID registration,
      @RequestHeader(value = HttpHeaders.CONTENT_TYPE,
          defaultValue = MediaType.APPLICATION_JSON_VALUE) String contentType,
      @RequestBody String stateDocument) {

    log.debug("PUT state");

    stateService.putState(activityId, agent, stateId, registration, stateDocument, contentType);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Post a State document (merges with existing).
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the optional registration
   * @param stateDocument the state document
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
   *      Document</a>
   */
  @PostMapping(params = {"activityId", "agent", "stateId"})
  public ResponseEntity<Void> postState(@RequestParam(required = true) String activityId,
      @Valid @RequestParam(required = true) Agent agent,
      @RequestParam(required = true) String stateId,
      @RequestParam(required = false) UUID registration,
      @RequestHeader(value = HttpHeaders.CONTENT_TYPE,
          defaultValue = MediaType.APPLICATION_JSON_VALUE) String contentType,
      @RequestBody String stateDocument) {

    log.debug("POST state");

    stateService.postState(activityId, agent, stateId, registration, stateDocument, contentType);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Delete a single State document.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the optional registration
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
   *      Document</a>
   */
  @DeleteMapping(params = {"activityId", "agent", "stateId"})
  public ResponseEntity<Void> deleteState(@RequestParam(required = true) String activityId,
      @Valid @RequestParam(required = true) Agent agent,
      @RequestParam(required = true) String stateId,
      @RequestParam(required = false) UUID registration) {

    log.debug("DELETE state");

    stateService.deleteState(activityId, agent, stateId, registration);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Delete all State documents for an activity and agent.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param registration the optional registration
   * @return the ResponseEntity
   *
   * @see <a href=
   *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-delete">Multiple
   *      Document DELETE</a>
   */
  @DeleteMapping(params = {"activityId", "agent", "!stateId"})
  public ResponseEntity<Void> deleteStates(@RequestParam(required = true) String activityId,
      @Valid @RequestParam(required = true) Agent agent,
      @RequestParam(required = false) UUID registration) {

    log.debug("DELETE states");

    stateService.deleteStates(activityId, agent, registration);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
