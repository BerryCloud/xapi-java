/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import java.net.URI;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting multiple Statements.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">GET
 *      Statements</a>
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@Builder
@Getter
public class GetMoreStatementsRequest implements Request {

  @NonNull
  private final URI more;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return uriBuilder.replacePath(more.getPath()).replaceQuery(more.getQuery());

  }

  /**
   * Builder for GetMoreStatementsRequest.
   */
  public static class Builder {

    /**
     * Sets the more token.
     *
     * @param more The more token of the GetMoreStatementsRequest.
     *
     * @return This builder
     */
    public Builder more(String more) {

      this.more = URI.create(more);

      return this;

    }

    /**
     * Sets the more token.
     *
     * @param more The more token of the GetMoreStatementsRequest.
     *
     * @return This builder
     */
    public Builder more(URI more) {

      this.more = more;

      return this;

    }

    // This static class extends the lombok builder.

  }

}
