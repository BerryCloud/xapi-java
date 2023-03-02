package dev.learning.xapi.client;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * Request for posting a single agent profile.
 *
 * <p>
 * TODO add see
 * </p>
 *
 * @author Thomas Turrell-Croft
 */
@Getter
@SuperBuilder
public class PostAgentProfileRequest extends AgentProfileRequest {

  @Default
  private MediaType contentType = MediaType.APPLICATION_JSON;

  /**
   * The agent profile object to store.
   */
  @NonNull
  private final Object profile;


  @Override
  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

}
