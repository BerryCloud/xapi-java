/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static dev.learning.xapi.client.XapiClientConstants.ACTIVITIES_PROFILE_PATH;
import static dev.learning.xapi.client.XapiClientConstants.ACTIVITY_ID_PARAM;
import static dev.learning.xapi.client.XapiClientConstants.ACTIVITY_ID_TEMPLATE;
import static dev.learning.xapi.client.XapiClientConstants.PROFILE_ID_PARAM;
import static dev.learning.xapi.client.XapiClientConstants.PROFILE_ID_TEMPLATE;

import java.net.URI;
import java.util.Map;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.web.util.UriBuilder;

@SuperBuilder
abstract class ActivityProfileRequest implements Request {

  /** The Activity id associated with this Profile document. */
  @NonNull private final URI activityId;

  /** The profile id associated with this Profile document. */
  @NonNull private final String profileId;

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put(ACTIVITY_ID_PARAM, activityId);
    queryParams.put(PROFILE_ID_PARAM, profileId);

    return uriBuilder
        .path(ACTIVITIES_PROFILE_PATH)
        .queryParam(ACTIVITY_ID_PARAM, ACTIVITY_ID_TEMPLATE)
        .queryParam(PROFILE_ID_PARAM, PROFILE_ID_TEMPLATE);
  }

  /** Builder for ActivityProfileRequest. */
  public abstract static class Builder<C extends ActivityProfileRequest, B extends Builder<C, B>> {

    /**
     * Sets the activityId.
     *
     * @param activityId The activityId of the ActivityProfileRequest.
     * @return This builder
     * @see ActivityProfileRequest#activityId
     */
    public B activityId(String activityId) {

      this.activityId = URI.create(activityId);

      return self();
    }

    /**
     * Sets the activityId.
     *
     * @param activityId The activityId of the ActivityProfileRequest.
     * @return This builder
     * @see ActivityProfileRequest#activityId
     */
    public B activityId(URI activityId) {

      this.activityId = activityId;

      return self();
    }
  }
}
