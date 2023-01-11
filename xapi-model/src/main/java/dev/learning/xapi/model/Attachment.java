/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Locale;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Attachment object.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#attachments">xAPI
 *      Attachment</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class Attachment {

  /**
   * Identifies the usage of this Attachment.
   */
  @NotNull
  private URI usageType;

  /**
   * Display name of this Attachment.
   */
  @NotNull
  private LanguageMap display;

  /**
   * A description of the Attachment.
   */
  private LanguageMap description;

  /**
   * The content type of the Attachment.
   */
  @NotNull
  private String contentType;

  /**
   * The length of the Attachment data in octets.
   */
  @NotNull
  private Integer length;

  /**
   * The SHA-2 hash of the Attachment data.
   */
  @NotNull
  private String sha2;

  /**
   * An IRL at which the Attachment data can be retrieved, or from which it used to be retrievable.
   * <p>
   * <strong>Note:</strong> The required type in the specification is URL however, the Java URL type
   * performs DNS lookups when calling equals and hashcode.
   * </p>
   */
  private URI fileUrl;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Attachment.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Adds a display value.
     *
     * @param key The key of the entry.
     * @param value The value of the entry.
     *
     * @return This builder
     *
     * @see ActivityDefinition#description
     */
    public Builder singleDisplay(Locale key, String value) {

      if (this.display == null) {
        this.display = new LanguageMap();
      }

      this.display.put(key, value);
      return this;
    }

    /**
     * Adds a description of the Attachment.
     *
     * @param key The key of the entry.
     * @param value The value of the entry.
     *
     * @return This builder
     *
     * @see ActivityDefinition#description
     */
    public Builder singleDescription(Locale key, String value) {
      if (this.description == null) {
        this.description = new LanguageMap();
      }

      this.description.put(key, value);
      return this;

    }
  }

}
