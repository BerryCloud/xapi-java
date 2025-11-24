/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import dev.learning.xapi.model.StatementFormat;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * GetStatementsRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("GetStatementsRequest Tests")
class GetStatementsRequestTests {

  @Test
  void whenBuildingGetStatementsRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building GetStatementsRequest With All Parameters
    GetStatementsRequest.Builder builder =
        GetStatementsRequest.builder()
            .agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))
            .verb(URI.create("http://adlnet.gov/expapi/verbs/answered"))
            .activity(URI.create("https://example.com/activity/1"))
            .registration(UUID.fromString("dbf5d9e8-d2aa-4d57-9754-b11e3f195fe3"))
            .relatedActivities(true)
            .relatedAgents(true)
            .since(Instant.parse("2016-01-01T00:00:00Z"))
            .until(Instant.parse("2018-01-01T00:00:00Z"))
            .limit(10)
            .format(StatementFormat.CANONICAL)
            .attachments(true)
            .ascending(true);

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);
  }
}
