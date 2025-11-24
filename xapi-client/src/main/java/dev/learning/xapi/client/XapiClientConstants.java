/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

/**
 * Constants used across xAPI client request classes.
 *
 * @author István Rátkai (Selindek)
 */
final class XapiClientConstants {

  /** Query parameter name for activityId. */
  static final String ACTIVITY_ID_PARAM = "activityId";

  /** URI template variable for activityId. */
  static final String ACTIVITY_ID_TEMPLATE = "{activityId}";

  /** Query parameter name for profileId. */
  static final String PROFILE_ID_PARAM = "profileId";

  /** URI template variable for profileId. */
  static final String PROFILE_ID_TEMPLATE = "{profileId}";

  /** Path segment for statements resource. */
  static final String STATEMENTS_PATH = "/statements";

  /** Path segment for activities resource. */
  static final String ACTIVITIES_PATH = "/activities";

  /** Path segment for activities profile resource. */
  static final String ACTIVITIES_PROFILE_PATH = "/activities/profile";

  /** Path segment for activities state resource. */
  static final String ACTIVITIES_STATE_PATH = "/activities/state";

  /** Path segment for agents profile resource. */
  static final String AGENTS_PROFILE_PATH = "/agents/profile";

  /** Query parameter name for agent. */
  static final String AGENT_PARAM = "agent";

  /** URI template variable for agent. */
  static final String AGENT_TEMPLATE = "{agent}";

  /** Query parameter name for registration. */
  static final String REGISTRATION_PARAM = "registration";

  /** Query parameter name for stateId. */
  static final String STATE_ID_PARAM = "stateId";

  /** URI template variable for stateId. */
  static final String STATE_ID_TEMPLATE = "{stateId}";

  /** Query parameter name for attachments. */
  static final String ATTACHMENTS_PARAM = "attachments";

  private XapiClientConstants() {
    // Private constructor to prevent instantiation
  }
}
