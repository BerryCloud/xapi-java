/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import dev.learning.xapi.model.validation.constraints.HasScheme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
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
public class Account implements Serializable {

  private static final long serialVersionUID = 3527735194079492745L;

  /**
   * The canonical home page for the system the account is on.
   */
  @NotNull
  @HasScheme
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

    // This static class extends the lombok builder.

  }

}
