/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import java.util.Map;
import java.util.Optional;
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

}
