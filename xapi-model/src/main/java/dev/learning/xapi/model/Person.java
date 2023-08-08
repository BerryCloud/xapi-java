/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.learning.xapi.model.validation.constraints.Mbox;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Setter;
import lombok.Value;

/**
 * This class represents the xAPI Person object.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#person-properties">xAPI
 *      Person</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(value = {"firstName", "lastName"})
public class Person {

  @Setter(AccessLevel.NONE)
  @Default
  private final ObjectType objectType = ObjectType.PERSON;

  /**
   * List of names.
   */
  private List<String> name;

  /**
   * List of e-mail addresses.
   */
  private List<@Mbox String> mbox;

  /**
   * List of the SHA1 hashes of mailto IRIs.
   */
  @JsonProperty("mbox_sha1sum")
  private List<String> mboxSha1sum;

  /**
   * List of openids that uniquely identify the Agents retrieved.
   */
  private List<URI> openid;

  /**
   * List of accounts.
   */
  @Valid
  private List<Account> account;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Person.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for account.
     *
     * @param account The Consumer Builder for account.
     *
     * @return This builder
     *
     * @see Person#account
     */
    public Builder addAccount(Consumer<Account.Builder> account) {

      final var builder = Account.builder();

      account.accept(builder);

      return addAccount(builder.build());
    }

    /**
     * Adds an account entry.
     *
     * @param account The account to add.
     *
     * @return This builder
     *
     * @see Person#account
     */
    public Builder addAccount(Account account) {
      if (this.account == null) {
        this.account = new ArrayList<>();
      }

      this.account.add(account);

      return this;

    }
  }

}
