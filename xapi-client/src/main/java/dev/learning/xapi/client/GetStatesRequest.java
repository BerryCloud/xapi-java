package dev.learning.xapi.client;


import java.util.List;

import org.springframework.http.HttpMethod;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GetStatesRequest extends StatesRequest<List<String>>{

  @Override
  protected HttpMethod method() {
    return HttpMethod.GET;
  }

}
