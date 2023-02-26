package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

@SuperBuilder
public class GetAgentProfileRequest extends AgentProfileRequest {

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

}
