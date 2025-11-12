/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.learning.xapi.client.GetActivityProfilesRequest.Builder;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * GetActivityProfilesRequest Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("GetActivityProfilesRequest Tests")
class GetActivityProfilesRequestTests {

  @Test
  void whenBuildingGetActivityProfilesRequestWithAllParametersThenNoExceptionIsThrown() {

    // When Building GetActivityProfilesRequest With All Parameters
    Builder builder = GetActivityProfilesRequest.builder()

        .activityId("https://example.com/activity/1")

        .since(Instant.parse("2016-01-01T00:00:00Z"));

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingGetActivityProfilesRequestWithActivityIdAsURITypeThenNoExceptionIsThrown() {

    // When Building GetActivityProfilesRequest With ActivityId As URI Type
    Builder builder = GetActivityProfilesRequest.builder()

        .activityId(URI.create("https://example.com/activity/1"));

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingGetActivityProfilesRequestWithoutRegistrationThenNoExceptionIsThrown() {

    // When Building GetActivityProfilesRequest Without Since
    Builder builder = GetActivityProfilesRequest.builder()

        .activityId("https://example.com/activity/1");

    // Then No Exception Is Thrown
    assertDoesNotThrow(builder::build);

  }

  @Test
  void whenBuildingGetActivityProfilesRequestWithoutActivityIdThenExceptionIsThrown() {

    // When Building GetActivityProfilesRequest Without ActivityId
    Builder builder = GetActivityProfilesRequest.builder();

    // Then NullPointerException Is Thrown
    assertThrows(NullPointerException.class, builder::build);

  }

  @Test
  void givenGetActivityProfilesRequestWithAllParametersWhenGettingURLThenResultIsExpected() {

    // Given GetActivityProfilesRequest With All Parameters
    GetActivityProfilesRequest request = GetActivityProfilesRequest.builder()

        .activityId("https://example.com/activity/1")

        .since(Instant.parse("2016-01-01T00:00:00Z"))

        .build();

    Map<String, Object> queryParams = new HashMap<>();

    // When Getting URL
    URI url =
        request.url(UriComponentsBuilder.fromUriString("https://example.com/xapi/"), queryParams)
            .build(queryParams);

    // Then Result Is Expected
    assertThat(url, is(URI.create(
        "https://example.com/xapi/activities/profile?activityId=https%3A%2F%2Fexample.com%2Factivity%2F1&since=2016-01-01T00%3A00%3A00Z")));

  }

}
