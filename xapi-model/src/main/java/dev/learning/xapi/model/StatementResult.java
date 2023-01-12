/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Statement Result object.
 *
 * @author Thomas Turrell-Croft
 * 
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#25-retrieval-of-statements">xAPI
 *      Statement Result</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_NULL) // Statements array could be empty
public class StatementResult {

  /**
   * List of Statements. Where no matching Statements are found, this property will contain an empty
   * array.
   */
  private Statement[] statements;

  /**
   * Relative IRL that can be used to fetch more results.
   */
  private URI more;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Statement.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for adding a statement.
     *
     * @param statement The Consumer Builder for statement.
     *
     * @return This builder
     *
     * @see StatementResult#statements
     */
    public Builder addStatement(Consumer<Statement.Builder> statement) {

      final Statement.Builder builder = Statement.builder();

      statement.accept(builder);

      return addStatement(builder.build());
    }

    /**
     * Adds a statement.
     *
     * @param statement The statement to add.
     *
     * @return This builder
     *
     * @see StatementResult#statements
     */
    public Builder addStatement(Statement statement) {

      if (statements == null) {
        statements = new Statement[] {statement};

        return this;
      }

      final List<Statement> list = new ArrayList<>(Arrays.asList(statements));
      list.add(statement);
      statements = list.toArray(statements);

      return this;

    }

  }

}
