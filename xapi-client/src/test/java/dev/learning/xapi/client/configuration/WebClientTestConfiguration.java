/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Test configuration to provide WebClient.Builder bean for tests.
 *
 * <p>In Spring Boot 4.0, WebClient.Builder autoconfiguration was moved/removed. This configuration
 * provides the bean for testing purposes.
 */
@Configuration
public class WebClientTestConfiguration {

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }
}
