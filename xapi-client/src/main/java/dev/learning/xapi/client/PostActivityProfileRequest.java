/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * Request for posting a single ActivityProfile.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete-1">Activity
 *      Profile POST</a>
 *
 * @author Thomas Turrell-Croft
 */
@SuperBuilder
@Getter
public class PostActivityProfileRequest extends ActivityProfileRequest {

  @Default
  private MediaType contentType = MediaType.APPLICATION_JSON;

  /**
   * The Activity Profile object to store.
   */
  @NonNull
  private final Object activityProfile;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

  /**
   * Builder for PostActivityProfileRequest.
   */
  public abstract static class Builder<C extends PostActivityProfileRequest,
      B extends Builder<C, B>> extends ActivityProfileRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }


}
