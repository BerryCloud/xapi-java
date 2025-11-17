/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
   * @param registration the registration
   * @return the state entity or Optional.empty() if not found
   */
  public Optional<StateEntity> getState(String activityId, Agent agent, String stateId,
      UUID registration) {

    log.info("get state: activityId={}, agent={}, stateId={}, registration={}", activityId, agent,
        stateId, registration);

    final String agentJson = serializeAgent(agent);
    final StateEntity.StateId id =
        new StateEntity.StateId(activityId, agentJson, stateId, registration);

    return repository.findById(id);
  }

  /**
   * Get all stateIds for an activity and agent.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param registration the registration
   * @return list of stateIds
   */
  public List<String> getStateIds(String activityId, Agent agent, UUID registration) {

    log.info("get state ids: activityId={}, agent={}, registration={}", activityId, agent,
        registration);

    final String agentJson = serializeAgent(agent);

    return repository
        .findByActivityIdAndAgentJsonAndRegistration(activityId, agentJson, registration).stream()
        .map(StateEntity::getStateId)
        .toList();
  }

  /**
   * Put a State document.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the registration
   * @param stateDocument the state document
   * @param contentType the content type
   */
  public void putState(String activityId, Agent agent, String stateId, UUID registration,
      String stateDocument, String contentType) {

    log.info("put state: activityId={}, agent={}, stateId={}, registration={}", activityId, agent,
        stateId, registration);

    final String agentJson = serializeAgent(agent);
    final StateEntity entity =
        new StateEntity(activityId, agentJson, stateId, registration, stateDocument, contentType);

    repository.save(entity);
  }

  /**
   * Post a State document (merges with existing).
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the registration
   * @param stateDocument the state document to merge
   * @param contentType the content type
   */
  public void postState(String activityId, Agent agent, String stateId, UUID registration,
      String stateDocument, String contentType) {

    log.info("post state: activityId={}, agent={}, stateId={}, registration={}", activityId, agent,
        stateId, registration);

    final String agentJson = serializeAgent(agent);
    final StateEntity.StateId id =
        new StateEntity.StateId(activityId, agentJson, stateId, registration);

    final Optional<StateEntity> existingEntity = repository.findById(id);

    String mergedDocument = stateDocument;
    String finalContentType = contentType;
    if (existingEntity.isPresent()) {
      // Merge the documents (for JSON objects, newer properties override)
      final String existing = existingEntity.get().getStateDocument();
      mergedDocument = mergeJsonDocuments(existing, stateDocument);
      // Keep the original content type if merging
      finalContentType = existingEntity.get().getContentType();
    }

    final StateEntity entity =
        new StateEntity(activityId, agentJson, stateId, registration, mergedDocument,
            finalContentType);

    repository.save(entity);
  }

  /**
   * Delete a single State document.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param stateId the stateId
   * @param registration the registration
   */
  public void deleteState(String activityId, Agent agent, String stateId, UUID registration) {

    log.info("delete state: activityId={}, agent={}, stateId={}, registration={}", activityId,
        agent, stateId, registration);

    final String agentJson = serializeAgent(agent);
    final StateEntity.StateId id =
        new StateEntity.StateId(activityId, agentJson, stateId, registration);

    repository.deleteById(id);
  }

  /**
   * Delete all State documents for an activity and agent.
   *
   * @param activityId the activityId
   * @param agent the agent
   * @param registration the registration
   */
  @Transactional
  public void deleteStates(String activityId, Agent agent, UUID registration) {

    log.info("delete states: activityId={}, agent={}, registration={}", activityId, agent,
        registration);

    final String agentJson = serializeAgent(agent);

    repository.deleteByActivityIdAndAgentJsonAndRegistration(activityId, agentJson, registration);
  }

  private String serializeAgent(Agent agent) {
    try {
      return mapper.writeValueAsString(agent);
    } catch (JsonProcessingException e) {
      log.error("Error serializing agent", e);
      throw new RuntimeException("Error serializing agent", e);
    }
  }

  private String mergeJsonDocuments(String existing, String update) {
    try {
      final JsonNode existingNode = mapper.readTree(existing);
      final JsonNode updateNode = mapper.readTree(update);

      if (existingNode.isObject() && updateNode.isObject()) {
        final ObjectNode result = mapper.createObjectNode();
        result.setAll((ObjectNode) existingNode);
        result.setAll((ObjectNode) updateNode);
        return mapper.writeValueAsString(result);
      }
      // If not both objects, the update replaces the existing
      return update;
    } catch (JsonProcessingException e) {
      log.error("Error merging JSON documents", e);
      // If merge fails, just return the update
      return update;
    }
  }
}
