/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import dev.learning.xapi.model.validation.constraints.ValidLocale;
import jakarta.validation.constraints.NotNull;
import java.util.Locale;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * This class represents the xAPI Interaction Component object.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#interaction-components">xAPI
 *      Interaction Components</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
@EqualsAndHashCode(of = "id")
public class InteractionComponent {

  /**
   * Identifies the interaction component within the list.
   */
  @NotNull
  private String id;

  /**
   * A description of the interaction component.
   */
  @ValidLocale
  private LanguageMap description;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Group.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Adds a description of the InteractionComponent.
     *
     * @param key The key of the entry.
     * @param value The value of the entry.
     *
     * @return This builder
     *
     * @see InteractionComponent#description
     */
    public Builder addDescription(Locale key, String value) {
      if (this.description == null) {
        this.description = new LanguageMap();
      }

      this.description.put(key, value);
      return this;
    }

  }

}
