/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * State Repository.
 *
 * @author Thomas Turrell-Croft
 */
public interface StateRepository extends CrudRepository<StateEntity, StateEntity.StateId> {

  /**
   * Find all stateIds for a given activityId and agent.
   *
   * @param activityId the activityId
   * @param agentJson the agent in JSON format
   * @param registration the registration (nullable)
   * @return list of stateIds
   */
  @Query("SELECT s.stateId FROM StateEntity s WHERE s.activityId = :activityId "
      + "AND s.agentJson = :agentJson "
      + "AND (:registration IS NULL OR s.registration = :registration)")
  List<String> findStateIds(@Param("activityId") String activityId,
      @Param("agentJson") String agentJson, @Param("registration") UUID registration);

  /**
   * Delete all states for a given activityId and agent.
   *
   * @param activityId the activityId
   * @param agentJson the agent in JSON format
   * @param registration the registration (nullable)
   */
  @Modifying
  @Query("DELETE FROM StateEntity s WHERE s.activityId = :activityId "
      + "AND s.agentJson = :agentJson "
      + "AND (:registration IS NULL OR s.registration = :registration)")
  void deleteByActivityIdAndAgentJson(@Param("activityId") String activityId,
      @Param("agentJson") String agentJson, @Param("registration") UUID registration);

}
