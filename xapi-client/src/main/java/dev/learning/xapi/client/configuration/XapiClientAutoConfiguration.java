/*
 * Copyright 2016-2019 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client.configuration;

import dev.learning.xapi.client.XapiClient;
import java.util.Base64;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Auto-configure {@link XapiClient}.
 *
 * @author István Rátkai (Selindek)
 */
@Configuration
@EnableConfigurationProperties(XapiClientProperties.class)
public class XapiClientAutoConfiguration {

  /**
   * Creates a default xAPI client bean.
   */
  @Bean
  @ConditionalOnMissingBean
  public XapiClient xapiClient(XapiClientProperties properties, WebClient.Builder builder,
      List<XapiClientConfigurer> configurers) {

    if (properties.getAuthorization() != null) {
      builder.defaultHeader(HttpHeaders.AUTHORIZATION, properties.getAuthorization());

    } else if (properties.getUsername() != null && properties.getPassword() != null) {
      final var auth = "basic " + Base64.getEncoder()
          .encodeToString((properties.getUsername() + ":" + properties.getPassword()).getBytes());
      builder.defaultHeader(HttpHeaders.AUTHORIZATION, auth);
    }

    if (properties.getBaseUrl() != null) {
      builder.baseUrl(properties.getBaseUrl());
    }

    configurers.forEach(c -> c.accept(builder));

    return new XapiClient(builder);

  }

}
