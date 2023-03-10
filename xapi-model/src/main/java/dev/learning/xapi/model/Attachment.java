/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Locale;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.codec.digest.DigestUtils;

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
   * The data of the attachment.
   * <p>
   * This is the actual String representation of the attachment as it appears in the http message.
   * </p>
   */
  @JsonIgnore
  private String data;
  
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
     * @see Attachment#description
     */
    public Builder addDisplay(Locale key, String value) {

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
     * @see Attachment#description
     */
    public Builder addDescription(Locale key, String value) {
      if (this.description == null) {
        this.description = new LanguageMap();
      }

      this.description.put(key, value);
      return this;

    }
    
    /**
     * <p>
     * Sets SHA-2 hash of the Attachment.
     * </p>
     * <p>
     * The sha2 is set ONLY if the data property was not set yet. 
     * (otherwise the sha2 is calculated automatically)
     * </p>
     *
     * @param sha2  The SHA-2 hash of the Attachment data.
     *
     * @return This builder
     */
    public Builder sha2(String sha2) {
      if (this.data == null) {
        this.sha2 = sha2;
      }
      
      return this;

    }
    
    /**
     * <p>
     * Sets data of the Attachment.
     * </p>
     * <p>
     * This method also automatically calculates the SHA-2 hash for the data.
     * </p>
     *
     * @param data The data of the Attachment as a String.
     *
     * @return This builder
     */
    public Builder data(String data) {
      this.data = data;
      if (data != null) {
        this.sha2 = DigestUtils.sha256Hex(data);
      } 
      
      return this;

    }
  

  }

}
