/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import java.util.Map;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Base class for xAPI request.
 *
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
abstract class Request {

  protected abstract UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams);

  /**
   * The request method.
   *
   * @return the request method as a {@link HttpMethod} object.
   */
  protected abstract HttpMethod getMethod();

  /**
   * Builder for Request.
   */
  public abstract static class Builder<C extends Request, B extends Builder<C, B>> {

    // This static class extends the lombok builder.

  }

}
