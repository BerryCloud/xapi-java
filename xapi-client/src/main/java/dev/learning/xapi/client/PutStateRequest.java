package dev.learning.xapi.client;

import org.springframework.http.HttpMethod;
import lombok.experimental.SuperBuilder;

@SuperBuilder()
public class PutStateRequest extends PostStateRequest{

  @Override
  protected HttpMethod method() {
    return HttpMethod.PUT;
  }
}
