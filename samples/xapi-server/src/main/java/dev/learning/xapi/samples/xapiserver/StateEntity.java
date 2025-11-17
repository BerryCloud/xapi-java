/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.UUID;

/**
 * StateEntity.
 *
 * @author Thomas Turrell-Croft
 */
@Entity
@IdClass(StateEntity.StateId.class)
public class StateEntity {

  @Id
  @Column(length = 2048)
  private String activityId;

  @Id
  @Column(length = 2048)
  private String agentJson;

  @Id
  private String stateId;

  @Id
  private UUID registration;

  @Lob
  @Column(columnDefinition = "CLOB")
  private String stateDocument;

  private String contentType;

  /**
   * StateEntity Constructor.
   */
  public StateEntity() {}

  /**
   * StateEntity Constructor.
   */
  public StateEntity(String activityId, String agentJson, String stateId, UUID registration,
      String stateDocument, String contentType) {
    this.activityId = activityId;
    this.agentJson = agentJson;
    this.stateId = stateId;
    this.registration = registration;
    this.stateDocument = stateDocument;
    this.contentType = contentType;
  }

  /**
   * Gets the activityId.
   *
   * @return the activityId
   */
  public String getActivityId() {
    return activityId;
  }

  /**
   * Sets the activityId.
   *
   * @param activityId the activityId to set
   */
  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  /**
   * Gets the agentJson.
   *
   * @return the agentJson
   */
  public String getAgentJson() {
    return agentJson;
  }

  /**
   * Sets the agentJson.
   *
   * @param agentJson the agentJson to set
   */
  public void setAgentJson(String agentJson) {
    this.agentJson = agentJson;
  }

  /**
   * Gets the stateId.
   *
   * @return the stateId
   */
  public String getStateId() {
    return stateId;
  }

  /**
   * Sets the stateId.
   *
   * @param stateId the stateId to set
   */
  public void setStateId(String stateId) {
    this.stateId = stateId;
  }

  /**
   * Gets the registration.
   *
   * @return the registration
   */
  public UUID getRegistration() {
    return registration;
  }

  /**
   * Sets the registration.
   *
   * @param registration the registration to set
   */
  public void setRegistration(UUID registration) {
    this.registration = registration;
  }

  /**
   * Gets the stateDocument.
   *
   * @return the stateDocument
   */
  public String getStateDocument() {
    return stateDocument;
  }

  /**
   * Sets the stateDocument.
   *
   * @param stateDocument the stateDocument to set
   */
  public void setStateDocument(String stateDocument) {
    this.stateDocument = stateDocument;
  }

  /**
   * Gets the contentType.
   *
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Sets the contentType.
   *
   * @param contentType the contentType to set
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Composite primary key for StateEntity.
   */
  public static class StateId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String activityId;
    private String agentJson;
    private String stateId;
    private UUID registration;

    /**
     * StateId Constructor.
     */
    public StateId() {}

    /**
     * StateId Constructor.
     */
    public StateId(String activityId, String agentJson, String stateId, UUID registration) {
      this.activityId = activityId;
      this.agentJson = agentJson;
      this.stateId = stateId;
      this.registration = registration;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof StateId)) {
        return false;
      }
      StateId stateId1 = (StateId) o;
      return activityId.equals(stateId1.activityId) && agentJson.equals(stateId1.agentJson)
          && stateId.equals(stateId1.stateId) && registration.equals(stateId1.registration);
    }

    @Override
    public int hashCode() {
      return activityId.hashCode() + agentJson.hashCode() + stateId.hashCode()
          + registration.hashCode();
    }

    /**
     * Gets the activityId.
     *
     * @return the activityId
     */
    public String getActivityId() {
      return activityId;
    }

    /**
     * Sets the activityId.
     *
     * @param activityId the activityId to set
     */
    public void setActivityId(String activityId) {
      this.activityId = activityId;
    }

    /**
     * Gets the agentJson.
     *
     * @return the agentJson
     */
    public String getAgentJson() {
      return agentJson;
    }

    /**
     * Sets the agentJson.
     *
     * @param agentJson the agentJson to set
     */
    public void setAgentJson(String agentJson) {
      this.agentJson = agentJson;
    }

    /**
     * Gets the stateId.
     *
     * @return the stateId
     */
    public String getStateId() {
      return stateId;
    }

    /**
     * Sets the stateId.
     *
     * @param stateId the stateId to set
     */
    public void setStateId(String stateId) {
      this.stateId = stateId;
    }

    /**
     * Gets the registration.
     *
     * @return the registration
     */
    public UUID getRegistration() {
      return registration;
    }

    /**
     * Sets the registration.
     *
     * @param registration the registration to set
     */
    public void setRegistration(UUID registration) {
      this.registration = registration;
    }
  }
}
