/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import dev.learning.xapi.model.ActivityState;
import dev.learning.xapi.model.Agent;
import jakarta.validation.Validator;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

/** State document storage for the sample LRS. */
@Service
@Transactional
public class StateService {

  private final StateRepository repository;
  private final ObjectMapper mapper;
  private final Validator validator;

  /**
   * Create the state service.
   *
   * @param repository state persistence
   * @param mapper the configured xAPI mapper
   * @param validator the configured xAPI validator
   */
  public StateService(StateRepository repository, ObjectMapper mapper, Validator validator) {
    this.repository = repository;
    this.mapper = mapper;
    this.validator = validator;
  }

  /**
   * Validate the request context and resolve its activity and agent identity.
   *
   * @param activityId the activity IRI
   * @param agentJson the JSON agent
   * @param registration the optional registration
   * @return the context identity digest
   */
  public String scope(String activityId, String agentJson, UUID registration) {
    try {
      if (activityId.isBlank() || !URI.create(activityId).isAbsolute()) {
        throw new IllegalArgumentException("activityId must be an absolute IRI");
      }
      Agent agent = mapper.readValue(agentJson, Agent.class);
      ActivityState context =
          ActivityState.builder().agent(agent).registration(registration).build();
      if (agent == null || !validator.validate(context).isEmpty()) {
        throw new IllegalArgumentException("Invalid agent or registration");
      }
      // Names and optional objectType do not form part of an agent's identity.
      if (agent.getMbox() != null) {
        return digest(activityId, "mbox", agent.getMbox());
      }
      if (agent.getMboxSha1sum() != null) {
        return digest(activityId, "mbox_sha1sum", agent.getMboxSha1sum());
      }
      if (agent.getOpenid() != null) {
        return digest(activityId, "openid", agent.getOpenid().toString());
      }
      if (agent.getAccount() != null) {
        return digest(
            activityId,
            "account",
            agent.getAccount().getHomePage().toString(),
            agent.getAccount().getName());
      }
      throw new IllegalArgumentException("Agent must have an identifier");
    } catch (JacksonException | IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid state context", e);
    }
  }

  /**
   * Retrieve a single state document.
   *
   * @param scope the context digest
   * @param registration the optional registration
   * @param stateId the document id
   * @return the document if present
   */
  @Transactional(readOnly = true)
  public Optional<StateEntity> get(String scope, UUID registration, String stateId) {
    return repository.findById(key(scope, registration, stateId));
  }

  /**
   * List documents, optionally limited to a registration and modification time.
   *
   * @param scope the context digest
   * @param registration the optional registration filter
   * @param since the exclusive modification time filter
   * @return the matching document metadata
   */
  @Transactional(readOnly = true)
  public List<StateRepository.StateMetadata> list(String scope, UUID registration, Instant since) {
    return repository.findMetadata(
        scope, registration == null ? null : registration.toString(), since);
  }

  /**
   * Store or merge a document, checking optional HTTP preconditions.
   *
   * @param scope the context digest
   * @param registration the optional registration
   * @param stateId the document id
   * @param contents document bytes
   * @param contentType document media type
   * @param merge whether this is a POST merge
   * @param ifMatch the optional If-Match header
   * @param ifNoneMatch the optional If-None-Match header
   */
  public void store(
      String scope,
      UUID registration,
      String stateId,
      byte[] contents,
      String contentType,
      boolean merge,
      String ifMatch,
      String ifNoneMatch) {
    StateEntity existing = get(scope, registration, stateId).orElse(null);
    checkPreconditions(existing, ifMatch, ifNoneMatch);
    if (merge) {
      ObjectNode posted = jsonObject(contents, contentType);
      if (existing != null) {
        ObjectNode stored = jsonObject(existing.getContents(), existing.getContentType());
        stored.setAll(posted);
        try {
          contents = mapper.writeValueAsBytes(stored);
        } catch (JacksonException e) {
          throw new IllegalStateException("Cannot serialize merged state", e);
        }
      }
    }
    StateEntity state = existing == null ? new StateEntity() : existing;
    state.setId(key(scope, registration, stateId));
    state.setScope(scope);
    state.setRegistration(registration == null ? null : registration.toString());
    state.setStateId(stateId);
    state.setContents(contents);
    state.setContentType(contentType);
    state.setUpdated(Instant.now());
    repository.saveAndFlush(state);
  }

  /**
   * Delete a single document or all documents in the supplied context.
   *
   * @param scope the context digest
   * @param registration the optional registration
   * @param stateId the optional document id
   * @param ifMatch the optional If-Match header for a single document
   * @param ifNoneMatch the optional If-None-Match header for a single document
   */
  public void delete(
      String scope, UUID registration, String stateId, String ifMatch, String ifNoneMatch) {
    if (stateId == null) {
      if (ifMatch != null || ifNoneMatch != null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Conditional deletion requires stateId");
      }
      repository.deleteAll(
          repository.findByScope(scope).stream()
              .filter(
                  state ->
                      registration == null
                          || registration.toString().equals(state.getRegistration()))
              .toList());
    } else {
      StateEntity state = get(scope, registration, stateId).orElse(null);
      checkPreconditions(state, ifMatch, ifNoneMatch);
      if (state != null) {
        repository.delete(state);
      }
    }
  }

  /**
   * Calculate the xAPI entity tag for document bytes.
   *
   * @param contents the response bytes
   * @return the quoted SHA-1 digest
   */
  public static String etag(byte[] contents) {
    return "\"" + hash("SHA-1", contents) + "\"";
  }

  private ObjectNode jsonObject(byte[] contents, String contentType) {
    try {
      MediaType type = MediaType.parseMediaType(contentType);
      if (!type.getType().equalsIgnoreCase("application")
          || !type.getSubtype().equalsIgnoreCase("json")) {
        throw new IllegalArgumentException("POST requires application/json documents");
      }
      var node =
          mapper.reader().with(DeserializationFeature.FAIL_ON_TRAILING_TOKENS).readTree(contents);
      if (node instanceof ObjectNode object) {
        return object;
      }
      throw new IllegalArgumentException("POST requires JSON objects");
    } catch (JacksonException | IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot merge state document", e);
    }
  }

  private void checkPreconditions(StateEntity state, String ifMatch, String ifNoneMatch) {
    String tag = state == null ? null : etag(state.getContents());
    if (ifMatch != null && (tag == null || !matches(ifMatch, tag, false))
        || ifNoneMatch != null && tag != null && matches(ifNoneMatch, tag, true)) {
      throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
    }
  }

  private boolean matches(String header, String tag, boolean weak) {
    return Arrays.stream(header.split(","))
        .map(String::trim)
        .anyMatch(
            value -> value.equals("*") || value.equals(tag) || weak && value.equals("W/" + tag));
  }

  private String key(String scope, UUID registration, String stateId) {
    return digest(scope, registration == null ? "" : registration.toString(), stateId);
  }

  private String digest(String... parts) {
    try {
      return hash("SHA-256", mapper.writeValueAsString(parts).getBytes(StandardCharsets.UTF_8));
    } catch (JacksonException e) {
      throw new IllegalStateException("Cannot serialize state identity", e);
    }
  }

  private static String hash(String algorithm, byte[] contents) {
    try {
      return HexFormat.of().formatHex(MessageDigest.getInstance(algorithm).digest(contents));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Missing digest algorithm", e);
    }
  }
}
