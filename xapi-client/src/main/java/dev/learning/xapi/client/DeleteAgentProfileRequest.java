package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for deleting a single agent profile.
 *
 * <p>
 * TODO add see
 * </p>
 *
 * @author Thomas Turrell-Croft
 */
@SuperBuilder
public class DeleteAgentProfileRequest extends AgentProfileRequest {

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.DELETE;
  }

}
