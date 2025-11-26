/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * State Repository.
 *
 * @author Thomas Turrell-Croft
 */
public interface StateRepository extends JpaRepository<StateEntity, StateEntity.StateId> {

  /**
   * Find all StateEntities by activityId, agentJson, and registration.
   *
   * @param activityId the activityId
   * @param agentJson the agent in JSON format
   * @param registration the registration
   * @return list of StateEntities
   */
  List<StateEntity> findByActivityIdAndAgentJsonAndRegistration(String activityId,
      String agentJson, UUID registration);

  /**
   * Delete all states for a given activityId, agentJson, and registration.
   *
   * @param activityId the activityId
   * @param agentJson the agent in JSON format
   * @param registration the registration
   */
  void deleteByActivityIdAndAgentJsonAndRegistration(String activityId, String agentJson,
      UUID registration);

}
