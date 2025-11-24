/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import tools.jackson.annotation.JsonInclude;
import tools.jackson.annotation.JsonInclude.Include;
import tools.jackson.annotation.JsonSubTypes;
import tools.jackson.annotation.JsonTypeInfo;
import tools.jackson.annotation.JsonTypeInfo.As;

/**
 * This interface represents the xAPI SubStatement object.
 *
 * <p><strong>Note:</strong> A SubStatement object cannot be a SubStatement.
 *
 * @author Thomas Turrell-Croft
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#substatements">xAPI
 *     SubStatements</a>
 */
@JsonInclude(Include.NON_EMPTY)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "objectType",
    defaultImpl = Activity.class,
    visible = true,
    include = As.EXISTING_PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Activity.class, name = "Activity"),
  @JsonSubTypes.Type(value = Agent.class, name = "Agent"),
  @JsonSubTypes.Type(value = Group.class, name = "Group"),
  @JsonSubTypes.Type(value = StatementReference.class, name = "StatementRef")
})
public interface SubStatementObject {}
