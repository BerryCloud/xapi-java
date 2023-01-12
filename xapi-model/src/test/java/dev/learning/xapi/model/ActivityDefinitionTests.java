/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Result Is Instance Of Activity Definition
    assertThat(result, instanceOf(ActivityDefinition.class));

  }

  @Test
  void whenDeserializingActivityDefinitionThenMoreInfoIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then MoreInfo Is Expected
    assertThat(result.getMoreInfo(), is(URI.create("http://example.com/more")));

  }

  @Test
  void whenDeserializingActivityDefinitionThenInteractionTypeIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then InteractionType Is Expected
    assertThat(result.getInteractionType(), is(InteractionType.TRUE_FALSE));

  }

  @Test
  void whenDeserializingActivityDefinitionThenCorrectResponsesPatternIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then CorrectResponsesPattern Is Expected
    assertThat(result.getCorrectResponsesPattern(), is(new String[] {
        "{case_matters=false}{lang=en}To store and provide access to learning experiences."}));

  }

  @Test
  void whenDeserializingActivityDefinitionThenExtensionsAreExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Extensions Are Expected
    assertThat(result.getExtensions().get(URI.create("http://url")), is("www.example.com"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenNameIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Name Is Expected
    assertThat(result.getName().get(Locale.ENGLISH),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenDescriptionIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Description Is Expected
    assertThat(result.getDescription().get(Locale.ENGLISH), is("pong[.]1:[,]dg[.]:10[,]lunch[.]"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenTypeIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Type Is Expected
    assertThat(result.getType(),
        is(URI.create("http://adlnet.gov/expapi/activities/cmi.interaction")));

  }

  @Test
  void whenDeserializingActivityDefinitionWithoutTypeWhenDeserializedThenTypeIsNull()
      throws Exception {

    final File file = ResourceUtils
        .getFile("classpath:activity_definition/activity_definition_without_type.json");

    // When Deserializing Activity Definition Without Type
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Type Is Null
    assertThat(result.getType(), nullValue());

  }

  @Test
  void whenDeserializingActivityDefinitionWithChoicesThenChoicesIDIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition With Choices
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Choices ID Is Expected
    assertThat(result.getChoices()[0].getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithChoicesThenChoicesDescriptionIsExpected()
      throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition With Choices
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Choices Description Is Expected
    assertThat(result.getChoices()[0].getDescription().get(Locale.ENGLISH),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithScaledThenScaleIDIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing ActivityDefinition With Scaled
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Scale ID Is Expected
    assertThat(result.getScale()[0].getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithScaledThenScaleDescriptionIsExpected()
      throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Scaled
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Scale Description Is Expected
    assertThat(result.getScale()[0].getDescription().get(Locale.ENGLISH),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithSourceThenSourceIDIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Source
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Source ID Is Expected
    assertThat(result.getSource()[0].getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithSourceThenSourceDescriptionIsExpected()
      throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Source
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Source Description Is Expected
    assertThat(result.getSource()[0].getDescription().get(Locale.ENGLISH),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithTargetThenTargetIDIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Target
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Target ID Is Expected
    assertThat(result.getTarget()[0].getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithTargetThenTargetDescriptionIsExpected()
      throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Target
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Target Description Is Expected
    assertThat(result.getTarget()[0].getDescription().get(Locale.ENGLISH),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithStepsThenStepsIDIsExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Steps
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Steps ID Is Expected
    assertThat(result.getSteps()[0].getId(), is("1"));

  }

  @Test
  void whenDeserializingActivityDefinitionWithStepsThenStepsDescriptionIsExpected()
      throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json");

    // When Deserializing Activity Definition With Steps
    final ActivityDefinition result = objectMapper.readValue(file, ActivityDefinition.class);

    // Then Steps Description Is Expected
    assertThat(result.getSteps()[0].getDescription().get(Locale.ENGLISH),
        is("Does the xAPI include the concept of statements?"));

  }

  @Test
  void whenSerializingActivityDefinitionOfInteractionTypeTrueFalseThenResultIsEqualToExpectedJson()
      throws IOException {

    final ActivityDefinition activityDefinition = ActivityDefinition.builder()

        .addName(Locale.ENGLISH, "True false question")

        .addDescription(Locale.ENGLISH, "Does the xAPI include the concept of statements?")

        .interactionType(InteractionType.TRUE_FALSE)

        .correctResponsesPattern(new String[] {"true"})

        .type(URI.create("http://adlnet.gov/expapi/activities/cmi.interaction"))

        .build();

    // When Serializing Activity Definition Of InteractionType True False
    final JsonNode result =
        objectMapper.readTree(objectMapper.writeValueAsString(activityDefinition));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(objectMapper
        .readTree(ResourceUtils.getFile("classpath:activity_definition/true_false.json"))));

  }

  @Test
  void whenSerializingActivityDefinitionOfInteractionTypeChoiceThenResultIsEqualToExpectedJson()
      throws IOException {

    final ActivityDefinition activityDefinition = ActivityDefinition.builder()

        .addName(Locale.ENGLISH, "Choice")

        .addDescription(Locale.ENGLISH,
            "Which of these prototypes are available at the beta site?")

        .interactionType(InteractionType.CHOICE)

        .correctResponsesPattern(new String[] {"golf[,]tetris"})

        .addChoice(c -> c.id("golf").addDescription(Locale.ENGLISH, "Golf Example"))

        .addChoice(c -> c.id("facebook").addDescription(Locale.ENGLISH, "Facebook App"))

        .addChoice(c -> c.id("tetris").addDescription(Locale.ENGLISH, "Tetris Example"))

        .addChoice(c -> c.id("scrabble").addDescription(Locale.ENGLISH, "Scrabble Example"))

        .type(URI.create("http://adlnet.gov/expapi/activities/cmi.interaction"))

        .build();

    // When Serializing Activity Definition Of InteractionType Choice
    final JsonNode result =
        objectMapper.readTree(objectMapper.writeValueAsString(activityDefinition));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(
        objectMapper.readTree(ResourceUtils.getFile("classpath:activity_definition/choice.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws Exception {

    final ActivityDefinition activityDefinition = objectMapper.readValue(
        ResourceUtils.getFile("classpath:activity_definition/activity_definition.json"),
        ActivityDefinition.class);

    // When Calling ToString
    final String result = activityDefinition.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "ActivityDefinition(name={en=Does the xAPI include the concept of statements?}, description={en=pong[.]1:[,]dg[.]:10[,]lunch[.]}, type=http://adlnet.gov/expapi/activities/cmi.interaction, moreInfo=http://example.com/more, interactionType=TRUE_FALSE, correctResponsesPattern=[{case_matters=false}{lang=en}To store and provide access to learning experiences.], choices=[InteractionComponent(id=1, description={en=Does the xAPI include the concept of statements?})], scale=[InteractionComponent(id=1, description={en=Does the xAPI include the concept of statements?})], source=[InteractionComponent(id=1, description={en=Does the xAPI include the concept of statements?})], target=[InteractionComponent(id=1, description={en=Does the xAPI include the concept of statements?})], steps=[InteractionComponent(id=1, description={en=Does the xAPI include the concept of statements?})], extensions={http://url=www.example.com})"));
  }

  // Then Null Pointer Is Thrown


}
