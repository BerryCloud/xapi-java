/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting a voided Statement.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#214-voided-statements">Voided
 *      Statements</a>
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
@Getter
public class GetVoidedStatementRequest extends GetStatementRequest {


  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return uriBuilder.path("statements")

        .queryParam("voidedStatementId", id)

        .queryParamIfPresent("format", Optional.ofNullable(format))

        .queryParamIfPresent("attachments", Optional.ofNullable(attachments));

  }

  /**
   * Builder for GetVoidedStatementRequest.
   */
  public abstract static class Builder<C extends GetVoidedStatementRequest, B extends Builder<C, B>>
      extends GetStatementRequest.Builder<C, B> {

    /**
     * Sets the voidedId.
     *
     * @param voidedId The voidedId of the GetVoidedStatementRequest.
     *
     * @return This builder
     */
    public Builder<C, B> voidedId(UUID voidedId) {
      id(voidedId);
      return self();
    }

    /**
     * Sets the voidedId.
     *
     * @param voidedId The voidedId of the GetVoidedStatementRequest.
     *
     * @return This builder
     */
    public Builder<C, B> voidedId(String voidedId) {
      id(voidedId);
      return self();
    }

    // This static class extends the lombok builder.

  }

}
