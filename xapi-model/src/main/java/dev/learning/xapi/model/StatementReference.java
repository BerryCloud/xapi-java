/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

/**
 * This class represents the xAPI Statement Reference object.
 *
 * @author Thomas Turrell-Croft
 * 
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#statement-references">xAPI
 *      Statement Reference</a>
 */
@Value
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "objectType")
public class StatementReference implements StatementObject, SubStatementObject {

  @Default
  private final ObjectType objectType = ObjectType.STATEMENTREF;

  /**
   * The UUID of a Statement.
   */
  @NotNull
  private UUID id;

  // **Warning** do not add fields that are not required by the xAPI specification.


  /**
   * Builder for StatementReference.
   */
  public static class Builder {

    // This static class extends the lombok builder.

  }


}
