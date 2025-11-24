/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.learning.xapi.client.DeleteActivityProfileRequest.Builder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * DeleteActivityProfileRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("DeleteActivityProfileRequest Tests")
class DeleteActivityProfileRequestTests {

  @Test
  void whenBuildingDeleteActivityProfileRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building DeleteActivityProfileRequest With All Parameters
    Builder<?, ?> builder =
        DeleteActivityProfileRequest.builder()
            .activityId(URI.create("https://example.com/activity/1"))
            .profileId("bookmark");

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);
  }

  @Test
  void whenBuildingDeleteActivityProfileRequestWithoutActivityIdThenExceptionIsThrown() {

    // When Building DeleteActivityProfileRequest Without ActivityId
    Builder<?, ?> builder = DeleteActivityProfileRequest.builder().profileId("bookmark");

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, builder::build);
  }

  @Test
  void whenBuildingDeleteActivityProfileRequestWithoutStateIdThenExceptionIsThrown() {

    // When Building DeleteActivityProfileRequest Without StateId
    Builder<?, ?> builder =
        DeleteActivityProfileRequest.builder()
            .activityId(URI.create("https://example.com/activity/1"));

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, builder::build);
  }

  @Test
  void givenDeleteActivityProfileRequestWithAllParametersWhenGettingURLThenResultIsExpected() {

    // Given DeleteActivityProfileRequest With All Parameters
    DeleteActivityProfileRequest request =
        DeleteActivityProfileRequest.builder()
            .activityId(URI.create("https://example.com/activity/1"))
            .profileId("bookmark")
            .build();

    Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    URI result =
        request
            .url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(
        result,
        is(
            URI.create(
                "https://example.com/xapi/activities/profile?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&profileId=bookmark")));
  }
}
