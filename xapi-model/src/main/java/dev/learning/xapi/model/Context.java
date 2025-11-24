/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.learning.xapi.jackson.LocaleSerializer;
import dev.learning.xapi.model.validation.constraints.HasScheme;
import dev.learning.xapi.model.validation.constraints.NotUndetermined;
import dev.learning.xapi.model.validation.constraints.ValidActor;
import dev.learning.xapi.model.validation.constraints.Variant;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Context object.
 *
 * @author Thomas Turrell-Croft
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#context">xAPI
 *     Context</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class Context {

  /** The registration that the Statement is associated with. */
  @Variant(2)
  private UUID registration;

  /** Instructor that the Statement relates to, if not included as the Actor of the Statement. */
  @Valid @ValidActor private Actor instructor;

  /** Team that this Statement relates to, if not included as the Actor of the Statement. */
  @Valid @ValidActor private Group team;

  /** A map of the types of learning activity context that this Statement is related to. */
  @Valid private ContextActivities contextActivities;

  /** Revision of the learning activity associated with this Statement. Format is free. */
  private String revision;

  /** Platform used in the experience of this learning activity. */
  private String platform;

  /** The language in which the experience being recorded in this Statement (mainly) occurred in. */
  @NotUndetermined
  @JsonSerialize(using = LocaleSerializer.class)
  private Locale language;

  /** Another Statement to be considered as context for this Statement. */
  @Valid private StatementReference statement;

  /** A map of any other domain-specific context relevant to this Statement. */
  private LinkedHashMap<@HasScheme URI, Object> extensions;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /** Builder for Context. */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for instructor.
     *
     * @param instructor The Consumer Builder for instructor.
     * @return This builder
     * @see Context#instructor
     */
    public Builder groupInstructor(Consumer<Group.Builder<?, ?>> instructor) {

      final Group.Builder<?, ?> builder = Group.builder();

      instructor.accept(builder);

      return instructor(builder.build());
    }

    /**
     * Sets the instructor.
     *
     * @param instructor The instructor of the Context.
     * @return This builder
     * @see Context#instructor
     */
    public Builder agentInstructor(Consumer<Agent.Builder<?, ?>> instructor) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      instructor.accept(builder);

      return instructor(builder.build());
    }

    /**
     * Consumer Builder for team.
     *
     * @param team The Consumer Builder for team.
     * @return This builder
     * @see Context#team
     */
    public Builder team(Consumer<Group.Builder<?, ?>> team) {

      final Group.Builder<?, ?> builder = Group.builder();

      team.accept(builder);

      return team(builder.build());
    }

    /**
     * Sets the team.
     *
     * @param team The team of the Context.
     * @return This builder
     * @see Context#team
     */
    public Builder team(Group team) {
      this.team = team;

      return this;
    }

    /**
     * Consumer Builder for contextActivities.
     *
     * @param contextActivities The Consumer Builder for contextActivities.
     * @return This builder
     * @see Context#contextActivities
     */
    public Builder contextActivities(Consumer<ContextActivities.Builder> contextActivities) {

      final var builder = ContextActivities.builder();

      contextActivities.accept(builder);

      return contextActivities(builder.build());
    }

    /**
     * Sets the contextActivities.
     *
     * @param contextActivities The contextActivities of the Context.
     * @return This builder
     * @see Context#contextActivities
     */
    public Builder contextActivities(ContextActivities contextActivities) {
      this.contextActivities = contextActivities;

      return this;
    }

    /**
     * Consumer Builder for statementReference.
     *
     * @param statementReference The Consumer Builder for statementReference.
     * @return This builder
     * @see Context#statement
     */
    public Builder statementReference(Consumer<StatementReference.Builder> statementReference) {

      final var builder = StatementReference.builder();

      statementReference.accept(builder);

      return statement(builder.build());
    }
  }
}
