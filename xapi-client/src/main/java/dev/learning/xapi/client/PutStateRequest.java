/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * Request for putting a single State document.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
 *      State Document PUT</a>
 *
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
@Getter
public class PutStateRequest extends StateRequest {

  @Default
  private MediaType contentType = MediaType.APPLICATION_JSON;

  /**
   * The state object to store.
   */
  @NonNull
  private final Object state;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.PUT;
  }

  /**
   * Builder for PutStateRequest.
   */
  public abstract static class Builder<C extends PutStateRequest, B extends Builder<C, B>>
      extends StateRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
