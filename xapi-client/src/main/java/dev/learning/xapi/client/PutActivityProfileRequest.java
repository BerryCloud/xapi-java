/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * Request for putting a single ActivityProfile.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete-1">Activity
 *      Profile PUT</a>
 *
 * @author Thomas Turrell-Croft
 */
@SuperBuilder
@Getter
public class PutActivityProfileRequest extends ActivityProfileRequest {

  @Default
  private MediaType contentType = MediaType.APPLICATION_JSON;

  /**
   * The Activity Profile object to store.
   */
  @NonNull
  private final Object activityProfile;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.PUT;
  }

  /**
   * Builder for PutActivityProfileRequest.
   */
  public abstract static class Builder<C extends PutActivityProfileRequest, B extends Builder<C, B>>
      extends ActivityProfileRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
