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
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-agent-or-profile-document-put--post--get--delete">POST
 *      Single Agent Profile Document</a>
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

  /**
   * Builder for PostAgentProfileRequest.
   */
  public abstract static class Builder<C extends PostAgentProfileRequest, B extends Builder<C, B>>
      extends AgentProfileRequest.Builder<C, B> {

    // This static class extends the lombok builder.

  }

}
