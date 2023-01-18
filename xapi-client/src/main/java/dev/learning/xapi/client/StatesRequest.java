package dev.learning.xapi.client;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.util.UriBuilder;

import lombok.Getter;
import lombok.NonNull;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
abstract class StatesRequest<T> extends XapiRequest<T>{

  @NonNull
  private URI activityId;
  
  @NonNull
  private String agent;

  @Default
  private Optional<UUID> registration = Optional.empty();

  protected String path() {
    return "activities/state";
  }
  
  @Override
  protected void query(UriBuilder uriBuilder, Map<String, Object> variableMap) {
    uriBuilder
    
    .queryParam("activityId", "{activityId}")

    .queryParam("agent", "{agent}")

    .queryParamIfPresent("registration", registration);
    
    variableMap.put("activityId", activityId);
    variableMap.put("agent", agent);
  }
}
