/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for deleting a single ActivityProfile.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete-1">Activity
 *      Profile DELETE</a>
 *
 * @author Thomas Turrell-Croft
 */
@SuperBuilder
public class DeleteActivityProfileRequest extends ActivityProfileRequest {

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.DELETE;
  }

}
