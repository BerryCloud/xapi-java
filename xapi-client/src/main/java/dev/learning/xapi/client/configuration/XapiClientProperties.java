/*
 * Copyright 2016-2019 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client.configuration;

import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Xapi client properties.
 *
 * @author István Rátkai (Selindek)
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "xapi.client")
public class XapiClientProperties {

  /**
   * The base URL for the xAPI client requests.
   */
  private URI baseUrl;

  /**
   * Username for basic authorization header.
   * <p>
   * Used only if {@link XapiClientProperties#password} is also set, AND
   * {@link XapiClientProperties#authorization} is NOT set.
   * </p>
   */
  private String username;

  /**
   * Password for basic authorization header.
   * <p>
   * Used only if {@link XapiClientProperties#username} is also set, AND
   * {@link XapiClientProperties#authorization} is NOT set.
   * </p>
   */
  private String password;

  /**
   * Authorization header.
   * <p>
   * This property has precedence over the {@link XapiClientProperties#username} and
   * {@link XapiClientProperties#password} properties.
   * </p>
   */
  private String authorization;

}
