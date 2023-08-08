/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * This interface represents the xAPI statement object.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#244-object">xAPI
 *      Object</a>
 */
@JsonInclude(Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "objectType", defaultImpl = Activity.class,
    visible = true, include = As.EXISTING_PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = Activity.class, name = "Activity"),
    @JsonSubTypes.Type(value = Agent.class, name = "Agent"),
    @JsonSubTypes.Type(value = Group.class, name = "Group"),
    @JsonSubTypes.Type(value = SubStatement.class, name = "SubStatement"),
    @JsonSubTypes.Type(value = StatementReference.class, name = "StatementRef")})
public interface StatementObject {

}
