/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.web.util.UriBuilder;

/**
 * Abstract superclass for state requests manipulating a single state document.
 *
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
@Getter
abstract class StateRequest extends StatesRequest {

  /**
   * The <strong>stateId</strong> query parameter.
   */
  @NonNull
  private final String stateId;

  @Override
  protected UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("stateId", stateId);

    return super.url(uriBuilder, queryParams).queryParam("stateId", "{stateId}");

  }

}
