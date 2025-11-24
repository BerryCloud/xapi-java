/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import tools.jackson.annotation.JsonInclude;
import tools.jackson.annotation.JsonInclude.Include;
import tools.jackson.databind.node.ObjectNode;
import dev.learning.xapi.model.validation.constraints.ValidActor;
import dev.learning.xapi.model.validation.constraints.Variant;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Activity State object.
 *
 * @author Thomas Turrell-Croft
 * @see <a href=
 *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#23-state-resource">xAPI
 *     Activity State</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ActivityState {

  private String activityId;

  private String stateId;

  @Valid @ValidActor private Agent agent;

  @Variant(2)
  private UUID registration;

  private ObjectNode state;

  // **Warning** do not add fields that are not required by the xAPI specification.

}
