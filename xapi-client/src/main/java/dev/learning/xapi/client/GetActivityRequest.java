/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import java.net.URI;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting a single Activity.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#full-activity-object-get">Full
 *      Activity Object GET</a>
 *
 * @author Thomas Turrell-Croft
 */
@Getter
@Builder
public class GetActivityRequest implements Request {

  @NonNull
  private URI activityId;

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("activityId", activityId);

    return uriBuilder.path("/activities").queryParam("activityId", "{activityId}");
  }

  @Override
  public HttpMethod getMethod() {

    return HttpMethod.GET;
  }

  /**
   * Builder for GetActivityRequest.
   */
  public static class Builder {

  }

}
