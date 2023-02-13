/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for deleting a single State document.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
 *      State Document DELETE</a>
 *
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
public class DeleteStateRequest extends StateRequest {

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.DELETE;
  }

  /**
   * Builder for DeleteStateRequest.
   */
  public abstract static class Builder<C extends DeleteStateRequest, B extends Builder<C, B>>
      extends StateRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
