/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import dev.learning.xapi.model.validation.constraints.ValidActor;
import dev.learning.xapi.model.validation.constraints.ValidAuthority;
import dev.learning.xapi.model.validation.constraints.ValidStatementPlatform;
import dev.learning.xapi.model.validation.constraints.ValidStatementRevision;
import dev.learning.xapi.model.validation.constraints.ValidStatementVerb;
import dev.learning.xapi.model.validation.constraints.Variant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;

/**
 * This class represents the xAPI Statement object.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 * 
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#statement-properties">xAPI
 *      Statement</a>
 */
@With
@Value
@ValidStatementPlatform
@ValidStatementRevision
@ValidStatementVerb
@Builder(toBuilder = true)
@JsonInclude(Include.NON_EMPTY)
@EqualsAndHashCode(of = {"actor", "verb", "object", "result", "context"})
public class Statement implements CoreStatement {

  /**
   * UUID assigned by LRS if not set by the Learning Record Provider.
   */
  @Variant(2)
  private UUID id;

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
   * Activity, Agent, or another Statement that is the Object of the Statement.
   */
  @NotNull
  @Valid
  @ValidActor
  private StatementObject object;

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
   * Timestamp of when this Statement was recorded.
   */
  private Instant stored;

  /**
   * Agent or Group who is asserting this Statement is true.
   */
  @ValidActor
  @ValidAuthority
  private Actor authority;

  /**
   * The Statement’s associated xAPI version.
   */
  @Pattern(regexp = "^1\\.0(\\.\\d)?$")
  private String version;

  /**
   * Headers for Attachments to the Statement.
   */
  @JsonFormat(without = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
  private List<Attachment> attachments;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Statement.
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
     * @see Statement#actor
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
     * @see Statement#actor
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
     * @see Statement#verb
     */
    public Builder verb(Consumer<Verb.Builder> verb) {

      final Verb.Builder builder = Verb.builder();

      verb.accept(builder);

      return verb(builder.build());
    }

    /**
     * Sets the verb.
     *
     * @param verb The definition of the Statement
     *
     * @return This builder
     *
     * @see Statement#verb
     */
    public Builder verb(Verb verb) {

      this.verb = verb;

      return this;
    }

    /**
     * Consumer Builder for result.
     *
     * @param result The Consumer Builder for result
     *
     * @return This builder
     *
     * @see Statement#result
     */
    public Builder result(Consumer<Result.Builder> result) {

      final Result.Builder builder = Result.builder();

      result.accept(builder);

      return result(builder.build());
    }

    /**
     * Sets the result.
     *
     * @param result The result of the Statement
     *
     * @return This builder
     *
     * @see Statement#result
     */
    public Builder result(Result result) {

      this.result = result;

      return this;
    }


    /**
     * Consumer Builder for context.
     *
     * @param authority The Consumer Builder for authority
     *
     * @return This builder
     *
     * @see Statement#authority
     */
    public Builder agentAuthority(Consumer<Agent.Builder<?, ?>> authority) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      authority.accept(builder);

      return authority(builder.build());
    }

    /**
     * Consumer Builder for activity object.
     *
     * @param activity The Consumer Builder for activity object
     *
     * @return This builder
     *
     * @see Statement#object
     */
    public Builder activityObject(Consumer<Activity.Builder> activity) {

      final Activity.Builder builder = Activity.builder();

      activity.accept(builder);

      return object(builder.build());
    }

    /**
     * Consumer Builder for statement reference object.
     *
     * @param statementReference The Consumer Builder for statement reference object
     *
     * @return This builder
     *
     * @see Statement#object
     */
    public Builder statementReferenceObject(
        Consumer<StatementReference.Builder> statementReference) {

      final StatementReference.Builder builder = StatementReference.builder();

      statementReference.accept(builder);

      return object(builder.build());
    }

    /**
     * Consumer Builder for context.
     *
     * @param context The Consumer Builder for context
     *
     * @return This builder
     *
     * @see Statement#context
     */
    public Builder context(Consumer<Context.Builder> context) {

      final Context.Builder builder = Context.builder();

      context.accept(builder);

      return context(builder.build());
    }

    /**
     * Sets the context.
     *
     * @param context The context of the Statement
     *
     * @return This builder
     *
     * @see Statement#context
     */
    public Builder context(Context context) {

      this.context = context;

      return this;
    }

    /**
     * Adds an attachment.
     *
     * @param attachment An {@link Attachment} object.
     *
     * @return This builder
     * 
     * @see Statement#attachments
     */
    public Builder addAttachment(Attachment attachment) {

      if (this.attachments == null) {
        this.attachments = new ArrayList<>();
      }

      this.attachments.add(attachment);
      return this;
    }
    
    /**
     * Consumer Builder for attachment.
     *
     * @param attachment The Consumer Builder for attachment
     *
     * @return This builder
     *
     * @see Statement#attachments
     */
    public Builder addAttachment(Consumer<Attachment.Builder> attachment) {

      final Attachment.Builder builder = Attachment.builder();

      attachment.accept(builder);

      return addAttachment(builder.build());
    }
  }

}
