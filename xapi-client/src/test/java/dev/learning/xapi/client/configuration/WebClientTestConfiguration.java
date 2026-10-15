/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client.configuration;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.webclient.autoconfigure.WebClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/** Use Boot's configured WebClient builder so tests exercise the application JSON codecs. */
@Configuration
@ImportAutoConfiguration(WebClientAutoConfiguration.class)
public class WebClientTestConfiguration {}
