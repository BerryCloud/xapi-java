/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for getting a single State document.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
 *      State Document GET</a>
 *
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
public class GetStateRequest extends StateRequest {

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  /**
   * Builder for GetStateRequest.
   */
  public abstract static class Builder<C extends GetStateRequest, B extends Builder<C, B>>
      extends StateRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
