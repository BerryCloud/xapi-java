/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.Statement;
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

  private final Statement[] statements;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return uriBuilder.path("statements");

  }

}
