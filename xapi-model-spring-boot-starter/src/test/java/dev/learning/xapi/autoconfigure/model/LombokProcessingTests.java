/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.autoconfigure.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests that Lombok annotation processing works correctly with xapi-model-spring-boot-starter. This
 * test ensures that third-party projects can use Lombok annotations without additional
 * configuration.
 *
 * @author GitHub Copilot
 */
class LombokProcessingTests {

  /**
   * Test class using Lombok @Getter and @Setter annotations. This simulates the Subscription class
   * mentioned in the issue.
   */
  @Getter
  @Setter
  static class TestSubscription {
    private String id;
    private String topic;
    private boolean active;
  }

  /**
   * Test class using Lombok @Builder annotation. This simulates the SubscriptionProperties class
   * mentioned in the issue.
   */
  @Builder
  @Getter
  static class TestSubscriptionProperties {
    private String endpoint;
    private int maxRetries;
    private long timeout;
  }

  /** Test class using Lombok @Value annotation for immutability. */
  @Value
  @Builder
  static class TestImmutableSubscription {
    String id;
    String topic;
    boolean active;
  }

  @Test
  @DisplayName("When Using Getter And Setter Then Lombok Processing Works")
  void testGetterSetterProcessing() {
    // Given
    TestSubscription subscription = new TestSubscription();

    // When
    subscription.setId("sub-123");
    subscription.setTopic("xapi-events");
    subscription.setActive(true);

    // Then
    assertThat(subscription.getId(), is("sub-123"));
    assertThat(subscription.getTopic(), is("xapi-events"));
    assertThat(subscription.isActive(), is(true));
  }

  @Test
  @DisplayName("When Using Builder Then Lombok Processing Works")
  void testBuilderProcessing() {
    // When
    TestSubscriptionProperties properties =
        TestSubscriptionProperties.builder()
            .endpoint("https://example.com/webhook")
            .maxRetries(3)
            .timeout(5000L)
            .build();

    // Then
    assertThat(properties, is(notNullValue()));
    assertThat(properties.getEndpoint(), is("https://example.com/webhook"));
    assertThat(properties.getMaxRetries(), is(3));
    assertThat(properties.getTimeout(), is(5000L));
  }

  @Test
  @DisplayName("When Using Value Then Lombok Processing Works")
  void testValueProcessing() {
    // When
    TestImmutableSubscription subscription =
        TestImmutableSubscription.builder()
            .id("sub-456")
            .topic("xapi-statements")
            .active(false)
            .build();

    // Then
    assertThat(subscription, is(notNullValue()));
    assertThat(subscription.getId(), is("sub-456"));
    assertThat(subscription.getTopic(), is("xapi-statements"));
    assertThat(subscription.isActive(), is(false));
  }
}
