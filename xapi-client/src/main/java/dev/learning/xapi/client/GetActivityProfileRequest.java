package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

@SuperBuilder
public class GetActivityProfileRequest extends ActivityProfileRequest {

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

}
