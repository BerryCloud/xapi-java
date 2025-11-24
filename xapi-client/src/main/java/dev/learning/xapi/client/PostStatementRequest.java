/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static dev.learning.xapi.client.XapiClientConstants.STATEMENTS_PATH;

import dev.learning.xapi.model.Statement;
import java.security.PrivateKey;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for posting multiple Statements.
 *
 * @see <a href=
 *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#212-post-statements">POST
 *     Statements</a>
 * @author Thomas Turrell-Croft
 */
@Builder
@Getter
public class PostStatementRequest implements Request {

  private final Statement statement;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return uriBuilder.path(STATEMENTS_PATH);
  }

  /** Builder for PostStatementRequest. */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for statement.
     *
     * @param statement The Consumer Builder for statement
     * @return This builder
     * @see PostStatementRequest#statement
     */
    public Builder statement(Consumer<Statement.Builder> statement) {

      final var builder = Statement.builder();

      statement.accept(builder);

      return statement(builder.build());
    }

    /**
     * Sets the statement.
     *
     * @param statement The Statement to post
     * @return This builder
     * @see PostStatementRequest#statement
     */
    public Builder statement(Statement statement) {

      this.statement = statement;

      return this;
    }

    /**
     * Consumer Builder for signed statement.
     *
     * @param statement The Consumer Builder for signed-statement
     * @param privateKey a PrivateKey for signing the Statement
     * @return This builder
     * @see PostStatementRequest#statement
     */
    public Builder signedStatement(Consumer<Statement.Builder> statement, PrivateKey privateKey) {

      final var builder = Statement.builder();

      statement.accept(builder);

      return statement(builder.signAndBuild(privateKey));
    }
  }
}
