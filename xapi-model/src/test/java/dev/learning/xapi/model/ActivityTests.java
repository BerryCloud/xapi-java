/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Activity Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("Activity tests")
class ActivityTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void whenDeserializingActivityThenResultIsInstanceOfActivity() throws Exception {

    final var file = ResourceUtils.getFile("classpath:activity/activity.json");

    // When Deserializing Activity
    final var result = objectMapper.readValue(file, Activity.class);

    // Then Result Is Instance Of Activity
    assertThat(result, instanceOf(Activity.class));

  }

  @Test
  void whenDeserializingActivityThenIdIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:activity/activity.json");

    // When Deserializing Activity
    final var result = objectMapper.readValue(file, Activity.class);

    // Then Id Is Expected
    assertThat(result.getId(), is(URI.create("http://example.com/xapi/activity/simplestatement")));

  }

  @Test
  void whenDeserializingActivityThenDefinitionIsInstanceOfActivityDefinition() throws Exception {

    final var file = ResourceUtils.getFile("classpath:activity/activity.json");

    // When Deserializing Activity
    final var result = objectMapper.readValue(file, Activity.class);

    // Then Definition Is Instance Of Activity Definition
    assertThat(result.getDefinition(), instanceOf(ActivityDefinition.class));

  }

  @Test
  void whenSerializingActivityThenResultIsEqualToExpectedJson() throws IOException {

    // This test uses the English locale because Jackson uses underscores instead of hyphens to
    // separate variants

    final var activity = Activity.builder()

        .id(URI.create("http://example.com/xapi/activity/simplestatement"))

        .definition(d -> d

            .addName(Locale.US, "simple statement")

            .addDescription(Locale.US,
                "A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object."))

        .build();

    // When Serializing Activity
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(activity));

    // Then Result Is Equal To Expected Json
    assertThat(result, is((objectMapper
        .readTree(ResourceUtils.getFile("classpath:activity/activity_with_object_type.json")))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final var activity = Activity.builder()

        .id(URI.create("http://www.example.co.uk/exampleactivity"))

        .definition(d -> d.addDescription(Locale.US,
            "A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object."))

        .build();

    // When Calling ToString
    final var result = activity.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "Activity(id=http://www.example.co.uk/exampleactivity, definition=ActivityDefinition(name=null, description={en_US=A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object.}, type=null, moreInfo=null, interactionType=null, correctResponsesPattern=null, choices=null, scale=null, source=null, target=null, steps=null, extensions=null))"));

  }

  /*
   * Constructor tests.
   */

  @Test
  void whenActivityIsConstructedWithIdThenIdIsExpected() {

    // When Activity Is Constructed With Id
    final var activity = new Activity("http://www.example.co.uk/exampleactivity");

    // Then Id Is Expected
    assertThat(activity.getId(), is(URI.create("http://www.example.co.uk/exampleactivity")));

  }

  /*
   * Builder tests.
   */

  @Test
  void whenActivityIsBuiltWithStringIdThenIdIsExpected() {

    // When Activity Is Built With String Id
    final var activity = Activity.builder().id("http://www.example.co.uk/exampleactivity").build();

    // Then Id Is Expected
    assertThat(activity.getId(), is(URI.create("http://www.example.co.uk/exampleactivity")));

  }

  @Test
  void whenValidatingActivityWithInvalidDisplayThenConstraintViolationsSizeIsOne()
      throws Exception {

    // This test uses Jackson to read the JSON because we need to test that Jackson does not use
    // Locale.forLanguageTag. Currently forLanguageTag is very permissive and will treat most
    // invalid strings as und.
    final var json =
        "{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"a12345678\":\"Simple Statement\"}}}";

    final var activity = objectMapper.readValue(json, Activity.class);

    // When Validating Activity With Invalid Display
    final Set<ConstraintViolation<Activity>> constraintViolations = validator.validate(activity);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

}
