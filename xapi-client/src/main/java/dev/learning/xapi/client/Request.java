/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Base class for xAPI request.
 *
 * @author István Rátkai (Selindek)
 */
interface Request {

  UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams);

  /**
   * The request method.
   *
   * @return the request method as a {@link HttpMethod} object.
   */
  HttpMethod getMethod();
}
