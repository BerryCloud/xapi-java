package dev.learning.xapi.client;


import org.springframework.http.HttpMethod;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GetStateRequest extends StateRequest<String>{

  @Override
  protected HttpMethod method() {
    return HttpMethod.GET;
  }

}
