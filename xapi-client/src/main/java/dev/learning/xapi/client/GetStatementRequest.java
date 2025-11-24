/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import static dev.learning.xapi.client.XapiClientConstants.ATTACHMENTS_PARAM;
import static dev.learning.xapi.client.XapiClientConstants.STATEMENTS_PATH;

import dev.learning.xapi.model.StatementFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting a Statement.
 *
 * @see <a href=
 *     "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">GET
 *     Statements</a>
 * @author Thomas Turrell-Croft
 */
@SuperBuilder
@Getter
public class GetStatementRequest implements Request {

  @NonNull protected final UUID id;

  protected final StatementFormat format;

  protected final Boolean attachments;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return uriBuilder
        .path(STATEMENTS_PATH)
        .queryParam("statementId", id)
        .queryParamIfPresent("format", Optional.ofNullable(format))
        .queryParamIfPresent(ATTACHMENTS_PARAM, Optional.ofNullable(attachments));
  }

  /** Builder for GetStatementRequest. */
  public abstract static class Builder<C extends GetStatementRequest, B extends Builder<C, B>> {

    /**
     * Sets the id.
     *
     * @param id The id of the GetStatementRequest.
     * @return This builder
     * @see GetStatementRequest#id
     */
    public Builder<C, B> id(UUID id) {
      this.id = id;
      return self();
    }

    /**
     * Sets the id.
     *
     * @param id The id of the GetStatementRequest.
     * @return This builder
     * @see GetStatementRequest#id
     */
    public Builder<C, B> id(String id) {
      this.id = UUID.fromString(id);
      return self();
    }

    // This static class extends the lombok builder.

  }
}
