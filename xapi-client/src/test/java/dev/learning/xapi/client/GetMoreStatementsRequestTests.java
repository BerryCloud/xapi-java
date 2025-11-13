/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * GetMoreStatementsRequest Tests.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@DisplayName("GetMoreStatementsRequest Tests")
class GetMoreStatementsRequestTests {

  @Test
  void whenBuildingGetMoreStatementsRequestWithUriParameterThenNoExceptionIsThrown() {

    // When Building GetMoreStatementsRequest With Uri Parameter
    GetMoreStatementsRequest.Builder builder = GetMoreStatementsRequest.builder()

        .more(
            URI.create("https://example.com/xapi/statements/869cc589-76fa-4283-8e96-eea86f9124e1"));

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingGetMoreStatementsRequestWithStringParameterThenNoExceptionIsThrown() {

    // When Building GetMoreStatementsRequest With String Parameter
    GetMoreStatementsRequest.Builder builder = GetMoreStatementsRequest.builder()

        .more("https://example.com/xapi/statements/869cc589-76fa-4283-8e96-eea86f9124e1");

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingGetMoreStatementsRequestWithoutMoreThenNullPointerExceptionIsThrown() {

    // When Building GetMoreStatementsRequest Without More
    GetMoreStatementsRequest.Builder builder = GetMoreStatementsRequest.builder();

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, builder::build);

  }


}
