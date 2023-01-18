package dev.learning.xapi.client;

import java.util.Map;

import org.springframework.web.util.UriBuilder;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
abstract class StateRequest<T> extends StatesRequest<T>{

  @NonNull
  private String stateId;

  @Override
  protected void query(UriBuilder uriBuilder, Map<String, Object> variableMap) {
    super.query(uriBuilder, variableMap);
    uriBuilder.queryParam("stateId", "{stateId}");
    variableMap.put("stateId", stateId);
  }
}
