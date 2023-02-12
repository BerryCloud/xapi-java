/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for deleting multiple State documents.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-delete">Multiple
 *      State Document DELETE</a>
 *
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
public class DeleteStatesRequest extends StatesRequest {

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.DELETE;
  }

  /**
   * Builder for DeleteStatesRequest.
   */
  public abstract static class Builder<C extends DeleteStatesRequest, B extends Builder<C, B>>
      extends StatesRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
