/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import tools.jackson.databind.ObjectMapper;

/**
 * Person Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("Person tests")
class PersonTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenDeserializingPersonWhenDeserializedThenResultIsInstanceOfPerson() throws Exception {

    final var file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final var result = objectMapper.readValue(file, Person.class);

    // Then Result Is Instance Of Person
    assertThat(result, instanceOf(Person.class));
  }

  @Test
  void whenDeserializingPersonWhenDeserializedThenNameIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final var result = objectMapper.readValue(file, Person.class);

    // Then Name Is Expected
    assertThat(result.getName(), is(Collections.singletonList("Normal Person")));
  }

  @Test
  void whenDeserializingPersonWhenDeserializedThenMboxIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final var result = objectMapper.readValue(file, Person.class);

    // Then Mbox Is Expected
    assertThat(result.getMbox(), is(Collections.singletonList("normal.person@normal.mail")));
  }

  @Test
  void whenDeserializingPersonWhenDeserializedThenShaSumIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final var result = objectMapper.readValue(file, Person.class);

    // Then ShaSum Is Expected
    assertThat(result.getMboxSha1sum(), is(Collections.singletonList("123")));
  }

  @Test
  void whenDeserializingPersonWhenDeserializedThenOpenidIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final var result = objectMapper.readValue(file, Person.class);

    // Then Openid Is Expected
    assertThat(
        result.getOpenid(), is(Collections.singletonList(URI.create("https://example.com"))));
  }

  @Test
  void whenDeserializingPersonThenAccountIsInstanceOfAccount() throws Exception {

    final var file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final var result = objectMapper.readValue(file, Person.class);

    // Then Account Is Instance Of Account
    assertThat(result.getAccount().get(0), instanceOf(Account.class));
  }

  @Test
  void whenSerializingPersonThenResultIsEqualToExpectedJson() throws IOException {

    final var person =
        Person.builder()
            .name(Collections.singletonList("Normal Person"))
            .mbox(Collections.singletonList("normal.person@normal.mail"))
            .mboxSha1sum(Collections.singletonList("123"))
            .openid(Collections.singletonList(URI.create("https://example.com")))
            .addAccount(a -> a.name("example1").homePage(URI.create("https://example.com")))
            .addAccount(a -> a.name("example2").homePage(URI.create("https://example.com")))
            .build();

    // When Serializing Person
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(person));

    // Then Result Is Equal To Expected Json
    assertThat(
        result, is(objectMapper.readTree(ResourceUtils.getFile("classpath:person/person.json"))));
  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final var person =
        objectMapper.readValue(ResourceUtils.getFile("classpath:person/person.json"), Person.class);

    // When Calling ToString
    final var result = person.toString();

    // Then Result Is Expected
    assertThat(
        result,
        is(
            "Person(objectType=Person, name=[Normal Person], mbox=[normal.person@normal.mail], mboxSha1sum=[123], openid=[https://example.com], account=[Account(homePage=https://example.com, name=example1), Account(homePage=https://example.com, name=example2)])"));
  }
}
