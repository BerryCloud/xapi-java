/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for posting multiple Statements.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#212-post-statements">POST
 *      Statements</a>
 *
 * @author Thomas Turrell-Croft
 */
@Builder
@Getter
public class PostStatementsRequest implements Request {

  private final StatementList statements;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return uriBuilder.path("/statements");

  }

  /**
   * Builder for PostStatementsRequest.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Sets the statements.
     *
     * @param statements The statements of the PostStatementsRequest.
     *
     * @return This builder
     *
     * @see PostStatementsRequest#statements
     */
    public Builder statements(List<Statement> statements) {
      this.statements = new StatementList(statements);
      return this;
    }

    /**
     * Sets the statements.
     *
     * @param statements The statements of the PostStatementsRequest.
     *
     * @return This builder
     *
     * @see PostStatementsRequest#statements
     */
    public Builder statements(Statement... statements) {
      this.statements = new StatementList(Arrays.asList(statements));
      return this;
    }

  }

  public static final class StatementList extends ArrayList<Statement> {

    private static final long serialVersionUID = 1013923414137485168L;

    public StatementList(Collection<? extends Statement> statements) {
      super(statements);
    }
  }
}
