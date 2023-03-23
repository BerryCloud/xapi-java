/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Activity Definition Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 * @author Thomas Turrell-Croft
 */
@DisplayName("ActivityDefinition tests")
class ActivityDefinitionTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenDeserializingActivityDefinitionThenResultIsInstanceOfActivityDefinition()
      throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Result Is Instance Of Activity Definition
    assertThat(result, instanceOf(ActivityDefinition.class));

  }

  @Test
  void whenDeserializingActivityDefinitionThenMoreInfoIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then MoreInfo Is Expected
    assertThat(result.getMoreInfo(), is(URI.create("http://example.com/more")));

  }

  @Test
  void whenDeserializingActivityDefinitionThenInteractionTypeIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then InteractionType Is Expected
    assertThat(result.getInteractionType(), is(InteractionType.TRUE_FALSE));

  }

  @Test
  void whenDeserializingActivityDefinitionThenCorrectResponsesPatternIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then CorrectResponsesPattern Is Expected
    assertThat(result.getCorrectResponsesPattern(), is(Collections.singletonList(
        "{case_matters=false}{lang=en}To store and provide access to learning experiences.")));

  }

  @Test
  void whenDeserializingActivityDefinitionThenExtensionsAreExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Extensions Are Expected
    assertThat(result.getExtensions().get(URI.create("http://url")), is("www.example.com"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenNameIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Name Is Expected
    assertThat(result.getName().get(Locale.US),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenDescriptionIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Description Is Expected
    assertThat(result.getDescription().get(Locale.US), is("pong[.]1:[,]dg[.]:10[,]lunch[.]"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenTypeIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Type Is Expected
    assertThat(result.getType(),
        is(URI.create("http://adlnet.gov/expapi/activities/cmi.interaction")));

  }

  @Test
  void whenDeserializingActivityDefinitionWithoutTypeWhenDeserializedThenTypeIsNull()
      throws Exception {

    final var file = ResourceUtils
        .getFile("classpath:activity_definition/activity_definition_without_type.json");

    // When Deserializing Activity Definition Without Type
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Type Is Null
    assertThat(result.getType(), nullValue());

  }

  @Test
  void whenDeserializingActivityDefinitionWithChoicesThenChoicesIDIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition With Choices
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Choices ID Is Expected
    assertThat(result.getChoices().get(0).getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithChoicesThenChoicesDescriptionIsExpected()
      throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition With Choices
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Choices Description Is Expected
    assertThat(result.getChoices().get(0).getDescription().get(Locale.US),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithScaledThenScaleIDIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition With Scaled
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Scale ID Is Expected
    assertThat(result.getScale().get(0).getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithScaledThenScaleDescriptionIsExpected()
      throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Scaled
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Scale Description Is Expected
    assertThat(result.getScale().get(0).getDescription().get(Locale.US),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithSourceThenSourceIDIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Source
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Source ID Is Expected
    assertThat(result.getSource().get(0).getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithSourceThenSourceDescriptionIsExpected()
      throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Source
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Source Description Is Expected
    assertThat(result.getSource().get(0).getDescription().get(Locale.US),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithTargetThenTargetIDIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Target
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Target ID Is Expected
    assertThat(result.getTarget().get(0).getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithTargetThenTargetDescriptionIsExpected()
      throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Target
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Target Description Is Expected
    assertThat(result.getTarget().get(0).getDescription().get(Locale.US),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithStepsThenStepsIDIsExpected() throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Steps
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Steps ID Is Expected
    assertThat(result.getSteps().get(0).getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithStepsThenStepsDescriptionIsExpected()
      throws Exception {

    final var file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Steps
    final var result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Steps Description Is Expected
    assertThat(result.getSteps().get(0).getDescription().get(Locale.US),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenSerializingActivityDefinitionOfInteractionTypeTrueFalseThenResultIsEqualToExpectedJson()
      throws IOException {

    final var activityDefinition = ActivityDefinition.builder()

        .addName(Locale.US, "True false question")

        .addDescription(Locale.US, "Does the xAPI include the concept of statements?")

        .interactionType(InteractionType.TRUE_FALSE)

        .correctResponsesPattern(Collections.singletonList("true"))

        .type(URI.create("http://adlnet.gov/expapi/activities/cmi.interaction"))

        .build();

    // When Serializing Activity Definition Of InteractionType True False
    final var result =
        objectMapper.readTree(objectMapper.writeValueAsString(activityDefinition));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(objectMapper
        .readTree(ResourceUtils.getFile("classpath:activity_definition/true_false.json"))));

  }

  @Test
  void whenSerializingActivityDefinitionOfInteractionTypeChoiceThenResultIsEqualToExpectedJson()
      throws IOException {

    final var activityDefinition = ActivityDefinition.builder()

        .addName(Locale.US, "Choice")

        .addDescription(Locale.US, "Which of these prototypes are available at the beta site?")

        .interactionType(InteractionType.CHOICE)

        .correctResponsesPattern(Collections.singletonList("golf[,]tetris"))

        .addChoice(c -> c.id("golf").addDescription(Locale.US, "Golf Example"))

        .addChoice(c -> c.id("facebook").addDescription(Locale.US, "Facebook App"))

        .addChoice(c -> c.id("tetris").addDescription(Locale.US, "Tetris Example"))

        .addChoice(c -> c.id("scrabble").addDescription(Locale.US, "Scrabble Example"))

        .type(URI.create("http://adlnet.gov/expapi/activities/cmi.interaction"))

        .build();

    // When Serializing Activity Definition Of InteractionType Choice
    final var result =
        objectMapper.readTree(objectMapper.writeValueAsString(activityDefinition));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(
        objectMapper.readTree(ResourceUtils.getFile("classpath:activity_definition/choice.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws Exception {

    final var activityDefinition = objectMapper.readValue(
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json"),
        ActivityDefinition.class);

    // When Calling ToString
    final var result = activityDefinition.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "ActivityDefinition(name={en_US=Does the xAPI include the concept of statements?}, description={en_US=pong[.]1:[,]dg[.]:10[,]lunch[.]}, type=http://adlnet.gov/expapi/activities/cmi.interaction, moreInfo=http://example.com/more, interactionType=TRUE_FALSE, correctResponsesPattern=[{case_matters=false}{lang=en}To store and provide access to learning experiences.], choices=[InteractionComponent(id=1, description={en_US=Does the xAPI include the concept of statements?})], scale=[InteractionComponent(id=1, description={en_US=Does the xAPI include the concept of statements?})], source=[InteractionComponent(id=1, description={en_US=Does the xAPI include the concept of statements?})], target=[InteractionComponent(id=1, description={en_US=Does the xAPI include the concept of statements?})], steps=[InteractionComponent(id=1, description={en_US=Does the xAPI include the concept of statements?})], extensions={http://url=www.example.com})"));
  }

  @Test
  void whenBuildingActivityDefinitionWithTwoNameValuesThenNameLanguageMapHasTwoEntries() {

    // When Building ActivityDefinition With Two Name Values
    final var activityDefinition = ActivityDefinition.builder()

        .addName(Locale.US, "True false question")

        .addName(Locale.GERMAN, "Richtig / Falsch-Frage")

        .addDescription(Locale.US, "Does the xAPI include the concept of statements?")

        .interactionType(InteractionType.TRUE_FALSE)

        .correctResponsesPattern(Collections.singletonList("true"))

        .type(URI.create("http://adlnet.gov/expapi/activities/cmi.interaction"))

        .build();

    // Then Name Language Map Has Two Entries
    assertThat(activityDefinition.getName(), aMapWithSize(2));

  }

  @Test
  void whenBuildingActivityDefinitionWithTwoDescriptionValuesThenDescriptionLanguageMapHasTwoEntries() {

    // When Building ActivityDefinition With Two Description Values
    final var activityDefinition = ActivityDefinition.builder()

        .addName(Locale.US, "True false question")

        .addDescription(Locale.US, "Does the xAPI include the concept of statements?")

        .addDescription(Locale.GERMAN, "Enthält die xAPI das Konzept von Anweisungen?")

        .interactionType(InteractionType.TRUE_FALSE)

        .correctResponsesPattern(Collections.singletonList("true"))

        .type(URI.create("http://adlnet.gov/expapi/activities/cmi.interaction"))

        .build();

    // Then Description Language Map Has Two Entries
    assertThat(activityDefinition.getDescription(), aMapWithSize(2));

  }

}
