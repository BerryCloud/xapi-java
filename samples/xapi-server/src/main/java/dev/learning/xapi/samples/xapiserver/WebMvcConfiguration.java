/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Registers request validation for the sample's statement endpoint. */
@Configuration(proxyBeanMethods = false)
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(new StatementQueryParametersInterceptor())
        .addPathPatterns("/xapi/statements");
  }
}
