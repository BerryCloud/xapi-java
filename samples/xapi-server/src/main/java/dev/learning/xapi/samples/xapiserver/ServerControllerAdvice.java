/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Handles exceptions in third party dependencies. */
@RestControllerAdvice
public class ServerControllerAdvice extends ResponseEntityExceptionHandler {

  /**
   * Reject concurrent state updates instead of overwriting another writer's document.
   *
   * @param e the concurrent update exception
   * @return the error response
   */
  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ErrorResponse handleConcurrentUpdate(OptimisticLockingFailureException e) {
    return ErrorResponse.builder(
            e,
            HttpStatus.PRECONDITION_FAILED,
            "The document changed during this request; retrieve it and retry")
        .build();
  }

  /**
   * Reject conflicting inserts, including concurrent creation of the same state document.
   *
   * @param e the conflicting insert exception
   * @return the error response
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ErrorResponse handleConflictingInsert(DataIntegrityViolationException e) {
    return ErrorResponse.builder(
            e, HttpStatus.CONFLICT, "The document could not be stored; retrieve it and retry")
        .build();
  }

  /**
   * Handles bean validation (JSR 380) exceptions and transforms them into errors that confirm to
   * RFC 7807.
   *
   * @param request the HTTP servlet request
   * @param e the throwable exception
   * @return the error response
   */
  @ResponseBody
  @ExceptionHandler(ConstraintViolationException.class)
  public ErrorResponse handleControllerException(HttpServletRequest request, Throwable e) {

    // RFC 7807 error response.
    return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage()).build();
  }
}
