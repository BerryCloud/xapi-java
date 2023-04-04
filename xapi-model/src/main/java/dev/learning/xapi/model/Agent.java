/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import dev.learning.xapi.jackson.StrictObjectTypeResolverBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the xAPI Agent object.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#agent">xAPI Agent</a>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(value = {"firstName", "lastName"})
@JsonTypeResolver(StrictObjectTypeResolverBuilder.class)
public class Agent extends Actor {

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Agent.
   */
  public abstract static class Builder<C extends Agent, B extends Builder<C, B>>
      extends Actor.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
