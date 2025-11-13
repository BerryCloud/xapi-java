/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handles exceptions in third party dependencies.
 */
@RestControllerAdvice
public class ServerControllerAdvice extends ResponseEntityExceptionHandler {

  /**
   * Handles bean validation (JSR 380) exceptions and transforms them into errors that confirm to
   * RFC 7807.
   */
  @ResponseBody
  @ExceptionHandler(ConstraintViolationException.class)
  public ErrorResponse handleControllerException(HttpServletRequest request, Throwable e) {

    // RFC 7807 error response.
    return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage())

        .build();
  }

}
