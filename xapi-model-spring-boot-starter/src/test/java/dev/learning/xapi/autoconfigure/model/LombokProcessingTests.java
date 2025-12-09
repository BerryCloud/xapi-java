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

  /** Test class using Lombok @Getter and @Setter annotations. */
  @Getter
  @Setter
  static class Person {
    private String name;
    private int age;
    private boolean active;
  }

  /** Test class using Lombok @Builder annotation. */
  @Builder
  @Getter
  static class Cat {
    private String name;
    private String breed;
    private int age;
  }

  /** Test class using Lombok @Value annotation for immutability. */
  @Value
  @Builder
  static class Dog {
    String name;
    String breed;
    int weight;
  }

  @Test
  @DisplayName("When Using Getter And Setter Then Lombok Processing Works")
  void testGetterSetterProcessing() {
    // Given
    Person person = new Person();

    // When
    person.setName("John Doe");
    person.setAge(30);
    person.setActive(true);

    // Then
    assertThat(person.getName(), is("John Doe"));
    assertThat(person.getAge(), is(30));
    assertThat(person.isActive(), is(true));
  }

  @Test
  @DisplayName("When Using Builder Then Lombok Processing Works")
  void testBuilderProcessing() {
    // When
    Cat cat = Cat.builder().name("Whiskers").breed("Siamese").age(3).build();

    // Then
    assertThat(cat, is(notNullValue()));
    assertThat(cat.getName(), is("Whiskers"));
    assertThat(cat.getBreed(), is("Siamese"));
    assertThat(cat.getAge(), is(3));
  }

  @Test
  @DisplayName("When Using Value Then Lombok Processing Works")
  void testValueProcessing() {
    // When
    Dog dog = Dog.builder().name("Buddy").breed("Golden Retriever").weight(65).build();

    // Then
    assertThat(dog, is(notNullValue()));
    assertThat(dog.getName(), is("Buddy"));
    assertThat(dog.getBreed(), is("Golden Retriever"));
    assertThat(dog.getWeight(), is(65));
  }
}
