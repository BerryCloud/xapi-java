/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Builder.Default;
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

  @Default
  private ObjectType objectType = ObjectType.PERSON;

  /**
   * List of names.
   */
  private String[] name;

  /**
   * List of e-mail addresses.
   */
  private String[] mbox;

  /**
   * List of the SHA1 hashes of mailto IRIs.
   */
  @JsonProperty("mbox_sha1sum")
  private String[] mboxSha1sum;

  /**
   * List of openids that uniquely identify the Agents retrieved.
   */
  private URI[] openid;

  /**
   * List of accounts.
   */
  @Valid
  private Account[] account;

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
    public Builder singleAccount(Consumer<Account.Builder> account) {

      final Account.Builder builder = Account.builder();

      account.accept(builder);

      return singleAccount(builder.build());
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
    public Builder singleAccount(Account account) {
      if (this.account == null) {
        this.account = new Account[] {account};

        return this;
      }

      final List<Account> list = new ArrayList<>(Arrays.asList(this.account));
      list.add(account);
      this.account = list.toArray(this.account);

      return this;

    }
  }

}
