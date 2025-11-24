/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.ResourceUtils;
import tools.jackson.databind.ObjectMapper;

/**
 * Agent Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 * @author Thomas Turrell-Croft
 */
@DisplayName("Agent tests")
class AgentTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @ParameterizedTest
  @ValueSource(
      strings = {
        "classpath:agent/agent.json",
        "classpath:agent/agent_without_object_type.json",
        "classpath:agent/agent_with_object_type.json"
      })
  void whenDeserializingAgentThenResultIsInstanceOfAgent(String fileName) throws IOException {

    final var file = ResourceUtils.getFile(fileName);

    // When Deserializing Agent
    final var result = objectMapper.readValue(file, Agent.class);

    // Then Result Is Instance Of Agent
    assertThat(result, instanceOf(Agent.class));
  }

  @Test
  void whenDeserializingAgentWithNameThenNameIsExpected() throws IOException {

    final var file = ResourceUtils.getFile("classpath:agent/agent_with_name.json");

    // When Deserializing Agent With Name
    final var result = objectMapper.readValue(file, Agent.class);

    // Then Name Is Expected
    assertThat(result.getName(), is("A N Other"));
  }

  @Test
  void whenDeserializingAgentWithMboxThenMboxIsExpected() throws IOException {

    final var file = ResourceUtils.getFile("classpath:agent/agent_with_mbox.json");

    // When Deserializing Agent With Mbox
    final var result = objectMapper.readValue(file, Agent.class);

    // Then Mbox Is Expected
    assertThat(result.getMbox(), is("mailto:other@example.com"));
  }

  @Test
  void whenDeserializingAgentWithMboxSha1sumThenMboxSha1sumIsExpected() throws IOException {

    final var file = ResourceUtils.getFile("classpath:agent/agent_with_mbox_sha1sum.json");

    // When Deserializing Agent With MboxSha1sum
    final var result = objectMapper.readValue(file, Agent.class);

    // Then MboxSha1sum Is Expected
    assertThat(result.getMboxSha1sum(), is("1234"));
  }

  @Test
  void whenDeserializingAgentWithOpenIdThenOpenIdIsExpected() throws IOException {

    final var file = ResourceUtils.getFile("classpath:agent/agent_with_openid.json");

    // When Deserializing Agent With OpenId
    final var result = objectMapper.readValue(file, Agent.class);

    // Then OpenId Is Expected
    assertThat(result.getOpenid(), is(URI.create("1234")));
  }

  @Test
  void whenDeserializingAgentWithAccountThenAccountIsInstanceOfAccount() throws IOException {

    final var file = ResourceUtils.getFile("classpath:agent/agent_with_account.json");

    // When Deserializing Agent With Account
    final var result = objectMapper.readValue(file, Agent.class);

    // Then Account Is Instance Of Account
    assertThat(result.getAccount(), instanceOf(Account.class));
  }

  @Test
  void whenSerializingAgentWithNameThenResultIsEqualToExpectedJson() throws IOException {

    final Agent agent = Agent.builder().name("A N Other").mbox("mailto:other@example.com").build();

    // When Serializing Agent With Name
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(agent));

    // Then Result Is Equal To Expected Json
    assertThat(
        result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:agent/agent_with_name.json"))));
  }

  @Test
  void whenSerializingAgentWithMboxThenResultIsEqualToExpectedJson() throws IOException {

    final Agent agent = Agent.builder().name("A N Other").mbox("mailto:other@example.com").build();

    // When Serializing Agent With Mbox
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(agent));

    // Then Result Is Equal To Expected Json
    assertThat(
        result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:agent/agent_with_mbox.json"))));
  }

  @Test
  void whenSerializingAgentWithMboxSha1sumThenResultIsEqualToExpectedJson() throws IOException {

    final Agent agent = Agent.builder().name("A N Other").mboxSha1sum("1234").build();

    // When Serializing Agent With MboxSha1sum
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(agent));

    // Then Result Is Equal To Expected Json
    assertThat(
        result,
        is(
            objectMapper.readTree(
                (ResourceUtils.getFile("classpath:agent/agent_with_mbox_sha1sum.json")))));
  }

  @Test
  void whenSerializingAgentWithOpenIdThenResultIsEqualToExpectedJson() throws IOException {

    final Agent agent = Agent.builder().name("A N Other").openid(URI.create("1234")).build();

    // When Serializing Agent With OpenId
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(agent));

    // Then Result Is Equal To Expected Json
    assertThat(
        result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:agent/agent_with_openid.json"))));
  }

  @Test
  void whenSerializingAgentWithAccountThenResultIsEqualToExpectedJson() throws IOException {

    final Agent agent =
        Agent.builder()
            .name("A N Other")
            .account(a -> a.name("another").homePage(URI.create("https://www.example.com")))
            .build();

    // when Serializing Agent With Account
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(agent));

    // Then Result Is Equal To Expected Json
    assertThat(
        result,
        is(
            objectMapper.readTree(
                ResourceUtils.getFile("classpath:agent/agent_with_account.json"))));
  }

  @Test
  void whenValidatingAgentWithOpenIdWithNoSchemeThenConstraintViolationsSizeIsOne() {

    final Agent agent = Agent.builder().name("A N Other").openid(URI.create("1234")).build();

    // When Validating Agent With OpenId With No Scheme
    final var result = validator.validate(agent);

    // Then Constraint Violations Size Is One
    assertThat(result.size(), is(1));
  }
}
