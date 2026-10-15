/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Persistence for state documents. */
public interface StateRepository extends JpaRepository<StateEntity, String> {

  /**
   * Delete documents directly without loading their contents.
   *
   * @param scope the activity and agent identity digest
   * @param registration the optional registration filter; null includes all registrations
   */
  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query(
      """
      delete from StateEntity state
      where state.scope = :scope
        and (:registration is null or state.registration = :registration)
      """)
  void deleteInScope(@Param("scope") String scope, @Param("registration") String registration);

  /**
   * Find listing metadata without loading document contents.
   *
   * @param scope the activity and agent identity digest
   * @param registration the optional registration filter
   * @param since the exclusive modification time filter
   * @return the matching document metadata
   */
  @Query(
      """
      select state.stateId as stateId, state.updated as updated
      from StateEntity state
      where state.scope = :scope
        and (:registration is null or state.registration = :registration)
        and (:since is null or state.updated > :since)
      """)
  List<StateMetadata> findMetadata(
      @Param("scope") String scope,
      @Param("registration") String registration,
      @Param("since") Instant since);

  /** The fields needed to list state documents and calculate response metadata. */
  interface StateMetadata {

    /**
     * Get the document id.
     *
     * @return the state id
     */
    String getStateId();

    /**
     * Get the document modification time.
     *
     * @return the last modification time
     */
    Instant getUpdated();
  }
}
