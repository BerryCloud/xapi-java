package dev.learning.xapi.client;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import dev.learning.xapi.model.Agent;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class StateParms {

  @NonNull
  private URI activityId;

  @NonNull
  private Agent agent;

  @NonNull
  private String stateId;

  @Default
  private Optional<UUID> registration = Optional.empty();

}
