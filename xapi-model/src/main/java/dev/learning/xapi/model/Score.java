/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import dev.learning.xapi.model.validation.constraints.ScaledScore;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Score object.
 *
 * @author Thomas Turrell-Croft
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#2451-score">xAPI
 *     Score</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class Score {

  /** The score related to the experience as modified by scaling and/or normalization. */
  @ScaledScore private Float scaled;

  /** The score achieved by the Actor in the experience described by the Statement. */
  private Float raw;

  /** The lowest possible score for the experience described by the Statement. */
  private Float min;

  /** The highest possible score for the experience described by the Statement. */
  private Float max;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /** Builder for Score. */
  public static class Builder {

    // This static class extends the lombok builder.

  }
}
