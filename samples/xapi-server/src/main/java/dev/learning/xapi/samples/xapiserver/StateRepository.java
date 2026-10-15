/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persistence for state documents. */
public interface StateRepository extends JpaRepository<StateEntity, String> {

  /**
   * Find all documents associated with an activity and agent.
   *
   * @param scope the activity and agent identity digest
   * @return the state documents, including documents from all registrations
   */
  List<StateEntity> findByScope(String scope);
}
