/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import dev.learning.xapi.model.validation.constraints.HasScheme;
import dev.learning.xapi.model.validation.constraints.VaildScore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Result object.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#result">xAPI
 *      Result</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class Result {

  /**
   * The score of the Agent in relation to the success or quality of the experience.
   */
  @Valid
  @VaildScore
  private Score score;

  /**
   * Indicates whether or not the attempt on the Activity was successful.
   */
  private Boolean success;

  /**
   * Indicates whether or not the Activity was completed.
   */
  private Boolean completion;

  /**
   * A response appropriately formatted for the given Activity.
   */
  private String response;

  /**
   * Period of time over which the Statement occurred.
   */
  // Java Duration does not store ISO 8601:2004 durations.
  @Pattern(regexp = "^(P\\d+W)?$|^P(?!$)(\\d+Y)?(\\d+M)?" // NOSONAR
      + "(\\d+D)?(T(?=\\d)(\\d+H)?(\\d+M)?(\\d*\\.?\\d+S)?)?$", // NOSONAR
      flags = Pattern.Flag.CASE_INSENSITIVE,
      message = "Must be a valid ISO 8601:2004 duration format.")
  private String duration;

  private LinkedHashMap<@HasScheme URI, Object> extensions;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Result.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for score.
     *
     * @param score The Consumer Builder for score.
     *
     * @return This builder
     *
     * @see Result#score
     */
    public Builder score(Consumer<Score.Builder> score) {

      final var builder = Score.builder();

      score.accept(builder);

      return score(builder.build());
    }

    /**
     * Sets the score.
     *
     * @param score The score of the Result.
     *
     * @return This builder
     *
     * @see Result#score
     */
    public Builder score(Score score) {

      this.score = score;

      return this;
    }

  }


}
