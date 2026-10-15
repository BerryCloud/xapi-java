/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Identifies a state document within an activity, agent and registration context. */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StateKey implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The registration parameter default for the unregistered context. */
  public static final String DEFAULT_REGISTRATION = "00000000-0000-0000-0000-000000000000";

  /** The reserved UUID identifying the unregistered context. */
  public static final UUID NIL_REGISTRATION = UUID.fromString(DEFAULT_REGISTRATION);

  @Column(nullable = false, length = 64)
  private String scope;

  @Column(nullable = false)
  private UUID registration;

  // H2's unbounded VARCHAR keeps long state ids indexable without the default 255-character limit.
  @Column(nullable = false, columnDefinition = "VARCHAR")
  private String stateId;

  /**
   * Create a document key from a validated request.
   *
   * @param scope the activity and agent identity digest
   * @param registration the non-null registration context
   * @param stateId the document id
   */
  public StateKey(String scope, UUID registration, String stateId) {
    this.scope = scope;
    this.registration = registration;
    this.stateId = stateId;
  }
}
