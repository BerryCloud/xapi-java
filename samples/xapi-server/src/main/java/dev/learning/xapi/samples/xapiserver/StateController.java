/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/** Basic implementation of the xAPI State Resource. */
@RestController
@RequestMapping("/xapi/activities/state")
public class StateController {

  private final StateService service;
  private final ObjectMapper mapper;

  /**
   * Create the state controller.
   *
   * @param service state storage
   * @param mapper the configured JSON mapper
   */
  public StateController(StateService service, ObjectMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  /**
   * Retrieve a document or a JSON array of state ids.
   *
   * @param activityId the activity IRI
   * @param agent the JSON agent
   * @param registration the optional registration
   * @param stateId the optional document id
   * @param since the exclusive time filter for listing ids
   * @return the document or state ids
   * @throws JacksonException if the state id array cannot be serialized
   */
  @GetMapping
  public ResponseEntity<byte[]> get(
      @RequestParam URI activityId,
      @RequestParam String agent,
      @RequestParam(required = false) UUID registration,
      @RequestParam(required = false) String stateId,
      @RequestParam(required = false) Instant since)
      throws JacksonException {
    String scope = service.scope(activityId.toString(), agent, registration);
    if (stateId != null) {
      if (since != null) {
        return ResponseEntity.badRequest().build();
      }
      return service
          .get(scope, registration, stateId)
          .map(
              state ->
                  ResponseEntity.ok()
                      .contentType(MediaType.parseMediaType(state.getContentType()))
                      .eTag(StateService.etag(state.getContents()))
                      .lastModified(state.getUpdated())
                      .body(state.getContents()))
          .orElseGet(() -> ResponseEntity.notFound().build());
    }
    var documents = service.list(scope, registration, since);
    byte[] ids =
        mapper.writeValueAsBytes(
            documents.stream().map(StateEntity::getStateId).distinct().sorted().toList());
    var response =
        ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).eTag(StateService.etag(ids));
    documents.stream()
        .map(StateEntity::getUpdated)
        .max(Instant::compareTo)
        .ifPresent(response::lastModified);
    return response.body(ids);
  }

  /**
   * Replace a state document with arbitrary bytes, or merge a JSON object using POST.
   *
   * @param activityId the activity IRI
   * @param agent the JSON agent
   * @param registration the optional registration
   * @param stateId the document id
   * @param contents document bytes
   * @param headers request headers including content type and optional preconditions
   * @param request the HTTP request used to distinguish PUT and POST
   * @return no content on success
   */
  @RequestMapping(method = {RequestMethod.PUT, RequestMethod.POST})
  public ResponseEntity<Void> store(
      @RequestParam URI activityId,
      @RequestParam String agent,
      @RequestParam(required = false) UUID registration,
      @RequestParam String stateId,
      @RequestBody(required = false) byte[] contents,
      @RequestHeader HttpHeaders headers,
      jakarta.servlet.http.HttpServletRequest request) {
    String scope = service.scope(activityId.toString(), agent, registration);
    MediaType type = headers.getContentType();
    service.store(
        scope,
        registration,
        stateId,
        contents == null ? new byte[0] : contents,
        type == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : type.toString(),
        request.getMethod().equals("POST"),
        headers.getFirst(HttpHeaders.IF_MATCH),
        headers.getFirst(HttpHeaders.IF_NONE_MATCH));
    return ResponseEntity.noContent().build();
  }

  /**
   * Delete a document or all state documents in the context.
   *
   * @param activityId the activity IRI
   * @param agent the JSON agent
   * @param registration the optional registration filter
   * @param stateId the optional document id
   * @param headers optional HTTP preconditions for single-document deletion
   * @return no content on success, including when the document does not exist
   */
  @DeleteMapping
  public ResponseEntity<Void> delete(
      @RequestParam URI activityId,
      @RequestParam String agent,
      @RequestParam(required = false) UUID registration,
      @RequestParam(required = false) String stateId,
      @RequestHeader HttpHeaders headers) {
    service.delete(
        service.scope(activityId.toString(), agent, registration),
        registration,
        stateId,
        headers.getFirst(HttpHeaders.IF_MATCH),
        headers.getFirst(HttpHeaders.IF_NONE_MATCH));
    return ResponseEntity.noContent().build();
  }
}
