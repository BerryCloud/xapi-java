/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.learning.xapi.model.validation.constraints.ValidActor;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Agent Profile object.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#agentprofres">xAPI
 *      Agent Profile</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class AgentProfile {

  @Valid
  @ValidActor
  private Agent agent;

  private String profileId;

  private ObjectNode profile;

  // **Warning** do not add fields that are not required by the xAPI specification.

}
