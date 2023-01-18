package dev.learning.xapi.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootConfiguration
@EnableAutoConfiguration
public class TestApp {

  @Value("${test.username:admin}")
  String username;
  
  @Value("${test.password:password}")
  String password;

  @Value("${test.url:http://localhost:8081/xapi/}")
  String url;
  
  @Bean
  public XapiClient xapiClient(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
    return new XapiClient(webClientBuilder.baseUrl(url)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "basic "+ Base64Utils.encodeToString((username+":"+password)
            .getBytes())), objectMapper);
  }

}
