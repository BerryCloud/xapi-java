/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for getting a single ActivityProfile.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete-1">Activity
 *      Profile GET</a>
 *
 * @author Thomas Turrell-Croft
 */
@SuperBuilder
public class GetActivityProfileRequest extends ActivityProfileRequest {

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  /**
   * Builder for GetActivityProfileRequest.
   */
  public abstract static class Builder<C extends GetActivityProfileRequest, B extends Builder<C, B>>
      extends ActivityProfileRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
