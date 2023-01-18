package dev.learning.xapi.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder()
@Getter
@EqualsAndHashCode(callSuper=true)
public class PostStateRequest extends DeleteStateRequest{

  @NonNull
  @Default
  private String contentType = MediaType.APPLICATION_JSON_VALUE;
  
  @NonNull
  private Object body;

  protected void headers(HttpHeaders headers) {
    super.headers(headers);
    headers.set(HttpHeaders.CONTENT_TYPE, contentType);
  }
  
  @Override
  protected HttpMethod method() {
    return HttpMethod.POST;
  }
  
 }
