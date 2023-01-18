package dev.learning.xapi.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.util.UriBuilder;

import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import reactor.core.publisher.Mono;

@SuperBuilder()
@RequiredArgsConstructor
abstract class XapiRequest<T> {
  
  @SuppressWarnings("unchecked")
  Mono<ResponseEntity<T>> execute(WebClient client) {
    
    RequestBodySpec r = client
     
      .method(method())
      
      .uri(uriBuilder-> {
        var variableMap = new HashMap<String, Object>();
        query(uriBuilder, variableMap);
        return uriBuilder.path(path()).build(variableMap); 
      })
      
      .headers(headers -> headers(headers))
      
      ;

    var body = getBody();
    
    if(body!=null) {
      r.bodyValue(body);
    }
    
    return r.retrieve().toEntity((Class<T>) (GenericTypeResolver.resolveTypeArgument(getClass(), XapiRequest.class)));

  }

  protected void query(UriBuilder uribuilder, Map<String, Object> variableMap) {}
  
  protected void headers(HttpHeaders headers) {}
  
  protected abstract HttpMethod method();
  
  protected abstract String path();
  
  protected Object getBody() {
    return null;
  }

}
