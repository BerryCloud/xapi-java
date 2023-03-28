/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client.configuration;

import dev.learning.xapi.client.XapiClient;
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
      builder
          .defaultHeaders(h -> h.setBasicAuth(properties.getUsername(), properties.getPassword()));
    }

    if (properties.getBaseUrl() != null) {
      builder.baseUrl(properties.getBaseUrl().toString());
    }

    configurers.forEach(c -> c.accept(builder));

    return new XapiClient(builder);

  }

}
