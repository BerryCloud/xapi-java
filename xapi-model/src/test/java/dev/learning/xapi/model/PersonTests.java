/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

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

    final File file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final Person result = objectMapper.readValue(file, Person.class);

    // Then Result Is Instance Of Person
    assertThat(result, instanceOf(Person.class));

  }

  @Test
  void whenDeserializingPersonWhenDeserializedThenNameIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final Person result = objectMapper.readValue(file, Person.class);

    // Then Name Is Expected
    assertThat(result.getName(), is(new String[] {"Normal Person"}));

  }

  @Test
  void whenDeserializingPersonWhenDeserializedThenMboxIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final Person result = objectMapper.readValue(file, Person.class);

    // Then Mbox Is Expected
    assertThat(result.getMbox(), is(new String[] {"normal.person@normal.mail"}));

  }

  @Test
  void whenDeserializingPersonWhenDeserializedThenShaSumIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final Person result = objectMapper.readValue(file, Person.class);

    // Then ShaSum Is Expected
    assertThat(result.getMboxSha1sum(), is(new String[] {"123"}));

  }

  @Test
  void whenDeserializingPersonWhenDeserializedThenOpenidIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final Person result = objectMapper.readValue(file, Person.class);

    // Then Openid Is Expected
    assertThat(result.getOpenid(), is(new URI[] {URI.create("https://example.com")}));

  }

  @Test
  void whenDeserializingPersonThenAccountIsInstanceOfAccount() throws Exception {

    final File file = ResourceUtils.getFile("classpath:person/person.json");

    // When Deserializing Person
    final Person result = objectMapper.readValue(file, Person.class);

    // Then Account Is Instance Of Account
    assertThat(result.getAccount()[0], instanceOf(Account.class));

  }

  @Test
  void whenSerializingPersonThenResultIsEqualToExpectedJson() throws IOException {

    final Person person = Person.builder()

        .name(new String[] {"Normal Person"})

        .mbox(new String[] {"normal.person@normal.mail"})

        .mboxSha1sum(new String[] {"123"})

        .openid(new URI[] {URI.create("https://example.com")})

        .singleAccount(a -> a.name("example1").homePage(URI.create("https://example.com")))

        .singleAccount(a -> a.name("example2").homePage(URI.create("https://example.com")))

        .build();

    // When Serializing Person
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(person));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:person/person.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final Person person =
        objectMapper.readValue(ResourceUtils.getFile("classpath:person/person.json"), Person.class);

    // When Calling ToString
    final String result = person.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "Person(objectType=PERSON, name=[Normal Person], mbox=[normal.person@normal.mail], mboxSha1sum=[123], openid=[https://example.com], account=[Account(homePage=https://example.com, name=example1), Account(homePage=https://example.com, name=example2)])"));

  }

}
