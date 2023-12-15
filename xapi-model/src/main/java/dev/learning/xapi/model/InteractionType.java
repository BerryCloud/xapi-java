/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * This enumeration class represents all valid xAPI interaction types.
 *
 * @author István Rátkai (Selindek)
 */
public enum InteractionType implements Serializable {

  /**
   * An interaction with two possible responses: true or false.
   */
  @JsonProperty("true-false")
  TRUE_FALSE,

  /**
   * An interaction with a number of possible choices from which the learner can select.
   */
  @JsonProperty("fill-in")
  FILL_IN,

  /**
   * An interaction which requires the learner to supply a response in the form of a long string of
   * characters.
   */
  @JsonProperty("long-fill-in")
  LONG_FILL_IN,

  /**
   * An interaction which asks the learner to select from a discrete set of choices on a scale.
   */
  @JsonProperty("likert")
  LIKERT,

  /**
   * An interaction where the learner is asked to match items in one set to items in another set.
   */
  @JsonProperty("matching")
  MATCHING,

  /**
   * An interaction that requires the learner to perform a task that requires multiple steps.
   */
  @JsonProperty("performance")
  PERFORMANCE,

  /**
   * An interaction where the learner is asked to order items in a set.
   */
  @JsonProperty("sequencing")
  SEQUENCING,

  /**
   * Any interaction which requires a numeric response from the learner.
   */
  @JsonProperty("numeric")
  NUMERIC,

  /**
   * An interaction with a number of possible choices from which the learner can select.
   */
  @JsonProperty("choice")
  CHOICE,

  /**
   * Another type of interaction that does not fit into those defined above.
   */
  @JsonProperty("other")
  OTHER;

  // **Warning** do not add fields that are not required by the xAPI specification.
}
