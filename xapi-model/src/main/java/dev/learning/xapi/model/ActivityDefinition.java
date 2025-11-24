/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import tools.jackson.annotation.JsonInclude;
import tools.jackson.annotation.JsonInclude.Include;
import tools.jackson.annotation.JsonMerge;
import dev.learning.xapi.model.validation.constraints.HasScheme;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Activity Definition object.
 *
 * <p>Upon receiving a Statement with an Activity Definition that differs from the one stored, an
 * LRS SHOULD ... change the definition and SHOULD update the stored Activity Definition.
 *
 * <p>When two ActivityDefinitions are merged, the properties and lists are replaced and the maps
 * are merged.
 *
 * @author Thomas Turrell-Croft
 * @see <a href=
 *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#activity-definition">xAPI
 *     Activity Definition</a>
 * @see <a href=
 *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#lrs-requirements-1">LRS
 *     Requirements</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ActivityDefinition {

  /** The human readable/visual name of the Activity. */
  @JsonMerge private LanguageMap name;

  /** A description of the Activity. */
  @JsonMerge private LanguageMap description;

  /** The type of Activity. */
  @HasScheme private URI type;

  /**
   * Resolves to a document with human-readable information about the Activity, which could include
   * a way to launch the activity.
   */
  @HasScheme private URI moreInfo;

  /** The type of interaction. */
  private InteractionType interactionType;

  /**
   * A pattern representing the correct response to the interaction. The structure of this pattern
   * varies depending on the interactionType.
   */
  private List<String> correctResponsesPattern;

  /** A list of the options available in the interaction for selection or ordering. */
  private List<InteractionComponent> choices;

  /** A list of the options on the likert scale. */
  private List<InteractionComponent> scale;

  /** Lists of sources to be matched. */
  private List<InteractionComponent> source;

  /** Lists of targets to be matched. */
  private List<InteractionComponent> target;

  /** A list of the elements making up the performance interaction. */
  private List<InteractionComponent> steps;

  /** A map of other properties as needed. */
  @JsonMerge private Map<@HasScheme URI, Object> extensions;

  // **Warning** do not add fields that are not required by the xAPI
  // specification.

  /** Builder for ActivityDefinition. */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Adds a human readable name of the Activity.
     *
     * @param key The key of the entry.
     * @param value The value of the entry.
     * @return This builder
     * @see ActivityDefinition#name
     */
    public Builder addName(Locale key, String value) {

      if (this.name == null) {
        this.name = new LanguageMap();
      }

      this.name.put(key, value);
      return this;
    }

    /**
     * Adds a description of the Activity.
     *
     * @param key The key of the entry.
     * @param value The value of the entry.
     * @return This builder
     * @see ActivityDefinition#description
     */
    public Builder addDescription(Locale key, String value) {
      if (this.description == null) {
        this.description = new LanguageMap();
      }

      this.description.put(key, value);
      return this;
    }

    /**
     * Consumer Builder for adding a choice option.
     *
     * @param interactionComponent The Consumer Builder for interactionComponent.
     * @return This builder
     * @see ActivityDefinition#choices
     */
    public Builder addChoice(Consumer<InteractionComponent.Builder> interactionComponent) {

      final var builder = InteractionComponent.builder();

      interactionComponent.accept(builder);

      return addChoice(builder.build());
    }

    /**
     * Adds a choice option.
     *
     * @param interactionComponent The interactionComponent to add.
     * @return This builder
     * @see ActivityDefinition#choices
     */
    public Builder addChoice(InteractionComponent interactionComponent) {

      if (choices == null) {
        choices = new ArrayList<>();
      }
      choices.add(interactionComponent);
      return this;
    }
  }
}
