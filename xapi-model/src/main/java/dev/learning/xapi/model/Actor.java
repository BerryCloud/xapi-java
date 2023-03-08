/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.function.Consumer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the xAPI Actor object.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#actor">xAPI Actor</a>
 */
@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
@EqualsAndHashCode(exclude = "name")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "objectType", 
    defaultImpl = Agent.class, include = As.PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = Agent.class, name = "Agent"),
    @JsonSubTypes.Type(value = Agent.class, name = "Person"),
    @JsonSubTypes.Type(value = Group.class, name = "Group")})
@JsonInclude(Include.NON_EMPTY)
public abstract class Actor implements StatementObject, SubStatementObject {

  /**
   * Full name.
   */
  String name;

  /**
   * An email address. The required format is "mailto:email address".
   */
  String mbox;

  /**
   * Hex-encoded SHA1 hash of a mailto IRI.
   */
  @JsonProperty("mbox_sha1sum")
  String mboxSha1sum;

  /**
   * An openID.
   */
  URI openid;

  /**
   * An account on an existing system e.g. an LMS or intranet.
   */
  @Valid
  Account account;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Actor.
   */
  public abstract static class Builder<C extends Actor, B extends Builder<C, B>> {

    /**
     * Consumer Builder for account.
     *
     * @param account The Consumer Builder for account.
     *
     * @return This builder
     *
     * @see Actor#account
     */
    public Builder<C, B> account(Consumer<Account.Builder> account) {

      final Account.Builder builder = Account.builder();

      account.accept(builder);

      return account(builder.build());
    }

    /**
     * Sets the account.
     *
     * @param account The Account of the Actor.
     *
     * @return This builder
     *
     * @see Actor#account
     */
    public Builder<C, B> account(Account account) {

      this.account = account;

      return self();
    }

  }

}
