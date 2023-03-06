package dev.learning.xapi.client;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@SuperBuilder
@Getter
public class PostActivityProfileRequest extends ActivityProfileRequest {

  @Default
  private MediaType contentType = MediaType.APPLICATION_JSON;

  /**
   * The Activity Profile object to store.
   */
  @NonNull
  private final Object activityProfile;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

}
