/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Account object.
 *
 * @author Thomas Turrell-Croft
 * 
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#2424-account-object">xAPI
 *      Account</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class Account {

  /**
   * The canonical home page for the system the account is on.
   */
  @NotNull
  private URI homePage;

  /**
   * The unique id or name used to log in to this account.
   */
  @NotBlank
  private String name;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Account.
   */
  public static class Builder {

    @NotNull
    private URI homePage;

    @NotBlank
    private String name;
    
    // This static class extends the lombok builder.

  }

}
