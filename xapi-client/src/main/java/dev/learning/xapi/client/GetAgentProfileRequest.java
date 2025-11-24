/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for getting a single agent profile.
 *
 * @see <a href=
 *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-agent-or-profile-document-put--post--get--delete">GET
 *     Single Agent Profile Document</a>
 * @author Thomas Turrell-Croft
 */
@SuperBuilder
public class GetAgentProfileRequest extends AgentProfileRequest {

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  /** Builder for GetAgentProfileRequest. */
  public abstract static class Builder<C extends GetAgentProfileRequest, B extends Builder<C, B>>
      extends AgentProfileRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }
}
