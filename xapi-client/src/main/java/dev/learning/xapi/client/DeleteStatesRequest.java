package dev.learning.xapi.client;

import org.springframework.http.HttpMethod;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder()
@EqualsAndHashCode(callSuper=true)
public class DeleteStatesRequest extends StatesRequest<Void>{

  @Override
  protected HttpMethod method() {
    return HttpMethod.DELETE;
  }
  
 }
