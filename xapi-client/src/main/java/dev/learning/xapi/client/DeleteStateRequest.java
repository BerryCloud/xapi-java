package dev.learning.xapi.client;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder()
@Getter
@EqualsAndHashCode(callSuper=true)
public class DeleteStateRequest extends StateRequest<Void>{

  private String match;

  private List<String> noneMatch;

  protected void headers(HttpHeaders headers) {
    if(match!=null) {
      headers.set(HttpHeaders.IF_MATCH, match);
    }
    if(noneMatch !=null && !noneMatch.isEmpty()) {
      headers.addAll(HttpHeaders.IF_NONE_MATCH, noneMatch);
    }
  }
  
  @Override
  protected HttpMethod method() {
    return HttpMethod.DELETE;
  }
  
 }
