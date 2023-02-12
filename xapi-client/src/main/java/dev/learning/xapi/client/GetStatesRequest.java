/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting multiple State documents.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-get">Multiple
 *      State Document GET</a>
 *
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
@Getter
public class GetStatesRequest extends StatesRequest {

  /**
   * Only ids of states stored since the specified instant (exclusive) are returned.
   */
  private final Instant since;

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  @Override
  protected UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return super.url(uriBuilder, queryParams).queryParamIfPresent("since",
        Optional.ofNullable(since));

  }

  /**
   * Builder for DeleteStateRequest.
   */
  public abstract static class Builder<C extends GetStatesRequest, B extends Builder<C, B>>
      extends StatesRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
