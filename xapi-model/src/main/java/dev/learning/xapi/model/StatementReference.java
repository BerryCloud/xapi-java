/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import dev.learning.xapi.jackson.StrictObjectTypeResolverBuilder;
import dev.learning.xapi.model.validation.constraints.Variant;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
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
@JsonTypeResolver(StrictObjectTypeResolverBuilder.class)
public class StatementReference implements StatementObject, SubStatementObject {

  /**
   * The UUID of a Statement.
   */
  @Variant(2)
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
