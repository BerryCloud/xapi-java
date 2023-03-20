/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Interaction Component Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 * @author Thomas Turrell-Croft
 */
@DisplayName("InteractionComponent tests")
class InteractionComponentTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void whenDeserializingInteractionComponentThenResultIsInstanceOfInteractionComponent()
      throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:interaction_component/interaction_component.json");

    // When Deserializing InteractionComponent
    final InteractionComponent result = objectMapper.readValue(file, InteractionComponent.class);

    // Then Result Is Instance Of InteractionComponent
    assertThat(result, instanceOf(InteractionComponent.class));

  }

  @Test
  void whenDeserializingInteractionComponentThenIDIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:interaction_component/interaction_component.json");

    // When Deserializing InteractionComponent
    final InteractionComponent result = objectMapper.readValue(file, InteractionComponent.class);

    // Then ID Is Expected
    assertThat(result.getId(), is("1"));

  }

  @Test
  void whenDeserializingInteractionComponentThenDescriptionIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:interaction_component/interaction_component.json");

    // When Deserializing InteractionComponent
    final InteractionComponent result = objectMapper.readValue(file, InteractionComponent.class);

    // Then Description Is Expected
    assertThat(result.getDescription().get(Locale.US), is("value"));

  }

  @Test
  void whenSerializingInteractionComponentThenResultIsEqualToExpectedJson() throws IOException {

    final InteractionComponent interactionComponent = InteractionComponent.builder()

        .id("1")

        .addDescription(Locale.US, "value")

        .build();

    // When Serializing InteractionComponent
    final JsonNode result =
        objectMapper.readTree(objectMapper.writeValueAsString(interactionComponent));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(objectMapper.readTree(
        ResourceUtils.getFile("classpath:interaction_component/interaction_component.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final InteractionComponent interactionComponent = objectMapper.readValue(
        ResourceUtils.getFile("classpath:interaction_component/interaction_component.json"),
        InteractionComponent.class);

    // When Calling ToString
    final String result = interactionComponent.toString();

    // Then Result Is Expected
    assertThat(result, is("InteractionComponent(id=1, description={en_US=value})"));

  }

  @Test
  void whenBuildingInteractionComponentWithTwoDescriptionValuesThenDisplayLanguageMapHasTwoEntries() {

    // When Building InteractionComponent With Two Description Values
    final InteractionComponent interactionComponent = InteractionComponent.builder().id("1")
        .addDescription(Locale.US, "value").addDescription(Locale.GERMAN, "Wert").build();

    // Then Description Language Map Has Two Entries
    assertThat(interactionComponent.getDescription(), aMapWithSize(2));

  }

  @Test
  void whenValidatingInteractionComponentWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {

    final InteractionComponent interactionComponent =
        InteractionComponent.builder().id("1").addDescription(Locale.US, "value").build();

    // When Validating Interaction Component With All Required Properties
    final Set<ConstraintViolation<InteractionComponent>> constraintViolations =
        validator.validate(interactionComponent);

    // Then ConstraintViolations Size Is Zero
    assertThat(constraintViolations, hasSize(0));

  }

  @Test
  void whenValidatingInteractionComponentWithoutIdThenConstraintViolationsSizeIsOne() {

    final InteractionComponent interactionComponent =
        InteractionComponent.builder().addDescription(Locale.US, "value").build();

    // When Validating Interaction Component Without Id
    final Set<ConstraintViolation<InteractionComponent>> constraintViolations =
        validator.validate(interactionComponent);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

}
