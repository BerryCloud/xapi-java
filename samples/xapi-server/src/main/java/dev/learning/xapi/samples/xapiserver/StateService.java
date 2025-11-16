/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Sample State Service.
 *
 * @author Thomas Turrell-Croft
 */
@Service
public class StateService {

  private final Logger log = LoggerFactory.getLogger(StateService.class);

  private final StateRepository repository;
  private final ObjectMapper mapper;

  /**
   * StateService Constructor.
   */
  public StateService(StateRepository repository, ObjectMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  /**
   * Get a single State document.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the registration (nullable)
   * @return the state document or Optional.empty() if not found
   */
  public Optional<JsonNode> getState(String activityId, Agent agent, String stateId,
      UUID registration) {

    log.info("get state: activityId={}, agent={}, stateId={}, registration={}", activityId, agent,
        stateId, registration);

    final String agentJson = serializeAgent(agent);
    final StateEntity.StateId id = new StateEntity.StateId(activityId, agentJson, stateId);

    return repository.findById(id).map(StateEntity::getStateDocument);
  }

  /**
   * Get all stateIds for an activity and agent.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param registration the registration (nullable)
   * @return list of stateIds
   */
  public List<String> getStateIds(String activityId, Agent agent, UUID registration) {

    log.info("get state ids: activityId={}, agent={}, registration={}", activityId, agent,
        registration);

    final String agentJson = serializeAgent(agent);

    return repository.findStateIds(activityId, agentJson, registration);
  }

  /**
   * Put a State document.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the registration (nullable)
   * @param stateDocument the state document
   */
  public void putState(String activityId, Agent agent, String stateId, UUID registration,
      JsonNode stateDocument) {

    log.info("put state: activityId={}, agent={}, stateId={}, registration={}", activityId, agent,
        stateId, registration);

    final String agentJson = serializeAgent(agent);
    final StateEntity entity =
        new StateEntity(activityId, agentJson, stateId, registration, stateDocument);

    repository.save(entity);
  }

  /**
   * Post a State document (merges with existing).
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the registration (nullable)
   * @param stateDocument the state document to merge
   */
  public void postState(String activityId, Agent agent, String stateId, UUID registration,
      JsonNode stateDocument) {

    log.info("post state: activityId={}, agent={}, stateId={}, registration={}", activityId, agent,
        stateId, registration);

    final String agentJson = serializeAgent(agent);
    final StateEntity.StateId id = new StateEntity.StateId(activityId, agentJson, stateId);

    final Optional<StateEntity> existingEntity = repository.findById(id);

    JsonNode mergedDocument = stateDocument;
    if (existingEntity.isPresent()) {
      // Merge the documents (for JSON objects, newer properties override)
      final JsonNode existing = existingEntity.get().getStateDocument();
      mergedDocument = mergeJsonNodes(existing, stateDocument);
    }

    final StateEntity entity =
        new StateEntity(activityId, agentJson, stateId, registration, mergedDocument);

    repository.save(entity);
  }

  /**
   * Delete a single State document.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the registration (nullable)
   */
  public void deleteState(String activityId, Agent agent, String stateId, UUID registration) {

    log.info("delete state: activityId={}, agent={}, stateId={}, registration={}", activityId,
        agent, stateId, registration);

    final String agentJson = serializeAgent(agent);
    final StateEntity.StateId id = new StateEntity.StateId(activityId, agentJson, stateId);

    repository.deleteById(id);
  }

  /**
   * Delete all State documents for an activity and agent.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param registration the registration (nullable)
   */
  @Transactional
  public void deleteStates(String activityId, Agent agent, UUID registration) {

    log.info("delete states: activityId={}, agent={}, registration={}", activityId, agent,
        registration);

    final String agentJson = serializeAgent(agent);

    repository.deleteByActivityIdAndAgentJson(activityId, agentJson, registration);
  }

  private String serializeAgent(Agent agent) {
    try {
      return mapper.writeValueAsString(agent);
    } catch (JsonProcessingException e) {
      log.error("Error serializing agent", e);
      throw new RuntimeException("Error serializing agent", e);
    }
  }

  private JsonNode mergeJsonNodes(JsonNode existing, JsonNode update) {
    if (existing.isObject() && update.isObject()) {
      final var existingObj = (com.fasterxml.jackson.databind.node.ObjectNode) existing;
      final var updateObj = (com.fasterxml.jackson.databind.node.ObjectNode) update;
      final var result = mapper.createObjectNode();
      result.setAll(existingObj);
      result.setAll(updateObj);
      return result;
    }
    // If not both objects, the update replaces the existing
    return update;
  }
}
