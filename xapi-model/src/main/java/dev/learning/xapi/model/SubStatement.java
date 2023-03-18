/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import dev.learning.xapi.model.validation.constraints.ValidActor;
import dev.learning.xapi.model.validation.constraints.ValidStatementPlatform;
import dev.learning.xapi.model.validation.constraints.ValidStatementRevision;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * This class represents the xAPI SubStatement object.
 *
 * @author Thomas Turrell-Croft
 * 
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#substatements">xAPI
 *      SubStatement</a>
 */
@Value
@Builder
@ValidStatementPlatform
@ValidStatementRevision
@EqualsAndHashCode(exclude = {"timestamp", "attachments"})
public class SubStatement implements StatementObject, CoreStatement {

  /**
   * Whom the Statement is about, as an Agent or Group Object.
   */
  @NotNull
  @Valid
  @ValidActor
  private Actor actor;

  /**
   * Action taken by the Actor.
   */
  @NotNull
  @Valid
  private Verb verb;

  /**
   * Activity or Agent.
   */
  @NotNull
  @Valid
  @ValidActor
  private SubStatementObject object;

  /**
   * Result Object, further details representing a measured outcome.
   */
  @Valid
  private Result result;

  /**
   * Context that gives the Statement more meaning.
   */
  @Valid
  private Context context;

  /**
   * Timestamp of when the events described within this Statement occurred.
   */
  private Instant timestamp;

  /**
   * Headers for Attachments to the Statement.
   */
  @Valid
  private List<Attachment> attachments;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for SubStatement.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for agent.
     *
     * @param agent The Consumer Builder for agent
     *
     * @return This builder
     *
     * @see SubStatement#actor
     */
    public Builder actor(Consumer<Agent.Builder<?, ?>> agent) {

      // TODO Handle a Group Builder

      final Agent.Builder<?, ?> builder = Agent.builder();

      agent.accept(builder);

      return actor(builder.build());
    }

    /**
     * Sets the agent.
     *
     * @param actor The actor of the Statement
     *
     * @return This builder
     *
     * @see SubStatement#actor
     */
    public Builder actor(Actor actor) {

      this.actor = actor;

      return this;
    }

    /**
     * Consumer Builder for verb.
     *
     * @param verb The Consumer Builder for verb
     *
     * @return This builder
     *
     * @see SubStatement#verb
     */
    public Builder verb(Consumer<Verb.Builder> verb) {

      final Verb.Builder builder = Verb.builder();

      verb.accept(builder);

      return verb(builder.build());
    }

    /**
     * Sets the verb.
     *
     * @param verb The definition of the SubStatement
     *
     * @return This builder
     *
     * @see SubStatement#verb
     */
    public Builder verb(Verb verb) {

      this.verb = verb;

      return this;
    }

  }

}
