/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import tools.jackson.annotation.JsonFormat;
import tools.jackson.annotation.JsonIgnore;
import dev.learning.xapi.model.validation.constraints.ValidActor;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the xAPI Group object.
 *
 * @author Thomas Turrell-Croft
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#group">xAPI Group</a>
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Group extends Actor {

  private final String objectType = "Group"; // NOSONAR

  /** The members of this Group. */
  @Valid
  @JsonFormat(without = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
  private final List<@ValidActor Agent> member;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /** Returns true if the group is anonymous. */
  @JsonIgnore
  public boolean isAnonymous() {

    return account == null
        && mbox == null
        && mboxSha1sum == null
        && openid == null
        && member != null
        && !member.isEmpty();
  }

  /** Builder for Group. */
  public abstract static class Builder<C extends Group, B extends Builder<C, B>>
      extends Actor.Builder<C, B> {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for member.
     *
     * @param member The Consumer Builder for member.
     * @return This builder
     * @see Group#member
     */
    public Builder<C, B> addMember(Consumer<Agent.Builder<?, ?>> member) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      member.accept(builder);

      return addMember(builder.build());
    }

    /**
     * Adds a member entry.
     *
     * @param agent The agent to add.
     * @return This builder
     * @see Group#member
     */
    public Builder<C, B> addMember(Agent agent) {

      if (member == null) {
        member = new ArrayList<>();
      }

      member.add(agent);

      return self();
    }
  }
}
