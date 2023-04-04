/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import dev.learning.xapi.jackson.StrictObjectTypeResolverBuilder;
import dev.learning.xapi.model.validation.constraints.ValidActor;
import dev.learning.xapi.model.validation.constraints.ValidStatementPlatform;
import dev.learning.xapi.model.validation.constraints.ValidStatementRevision;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
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
@JsonTypeResolver(StrictObjectTypeResolverBuilder.class)
public class SubStatement implements StatementObject, CoreStatement {

  /**
   * {@inheritDoc}
   */
  @NotNull
  @Valid
  @ValidActor
  private Actor actor;

  /**
   * {@inheritDoc}
   */
  @NotNull
  @Valid
  private Verb verb;

  /**
   * {@inheritDoc}
   * <p>
   * A SubStatement MUST NOT contain a SubStatement of its own, i.e., cannot be nested.
   * </p>
   */
  @NotNull
  @Valid
  @ValidActor
  private SubStatementObject object;

  /**
   * {@inheritDoc}
   */
  @Valid
  private Result result;

  /**
   * {@inheritDoc}
   */
  @Valid
  private Context context;

  /**
   * {@inheritDoc}
   */
  private Instant timestamp;

  /**
   * {@inheritDoc}
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

      final var builder = Verb.builder();

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

    /**
     * Consumer Builder for attachment.
     *
     * @param attachment The Consumer Builder for attachment
     *
     * @return This builder
     *
     * @see SubStatement#attachments
     */
    public Builder addAttachment(Consumer<Attachment.Builder> attachment) {

      final var builder = Attachment.builder();

      attachment.accept(builder);

      return addAttachment(builder.build());
    }

    /**
     * Adds an attachment.
     *
     * @param attachment An {@link Attachment} object.
     *
     * @return This builder
     *
     * @see SubStatement#attachments
     */
    public Builder addAttachment(Attachment attachment) {

      if (this.attachments == null) {
        this.attachments = new ArrayList<>();
      }

      this.attachments.add(attachment);
      return this;
    }
  }

}
