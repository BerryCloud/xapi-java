/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
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
 * ContextActivities Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("ContextActivities tests")
class ContextActivitiesTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenDeserializingContextActivitiesThenResultIsInstanceOfContextActivities()
      throws Exception {

    final File file = ResourceUtils.getFile("classpath:context_activities/context_activities.json");

    // When Deserializing ContextActivities
    final ContextActivities result = objectMapper.readValue(file, ContextActivities.class);

    // Then Result Is Instance Of ContextActivities
    assertThat(result, instanceOf(ContextActivities.class));

  }

  @Test
  void whenDeserializingContextActivitiesThenParentIsInstanceOfActivity() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context_activities/context_activities.json");

    // When Deserializing ContextActivities
    final ContextActivities result = objectMapper.readValue(file, ContextActivities.class);

    // Then Parent Is Instance Of Activity
    assertThat(result.getParent()[0], instanceOf(Activity.class));

  }

  @Test
  void whenDeserializingContextActivitiesThenGroupingIsInstanceOfActivity() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context_activities/context_activities.json");

    // When Deserializing ContextActivities
    final ContextActivities result = objectMapper.readValue(file, ContextActivities.class);

    // Then Grouping Is Instance Of Activity
    assertThat(result.getGrouping()[0], instanceOf(Activity.class));

  }

  @Test
  void whenDeserializingContextActivitiesThenCategoryIsInstanceOfActivity() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context_activities/context_activities.json");

    // When Deserializing ContextActivities
    final ContextActivities result = objectMapper.readValue(file, ContextActivities.class);

    // Then Category Is Instance Of Activity
    assertThat(result.getCategory()[0], instanceOf(Activity.class));

  }

  @Test
  void whenDeserializingContextActivitiesThenOtherIsInstanceOfActivity() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context_activities/context_activities.json");

    // When Deserializing ContextActivities
    final ContextActivities result = objectMapper.readValue(file, ContextActivities.class);

    // Then Other Is Instance Of Activity
    assertThat(result.getOther()[0], instanceOf(Activity.class));

  }

  @Test
  void whenSerializingAttachmentThenResultIsEqualToExpectedJson() throws IOException {

    final ContextActivities contextActivities = ContextActivities.builder()

        .singleParent(p -> p.id(URI.create("https://example.com/activity/1")))

        .singleGrouping(g -> g.id(URI.create("https://example.com/activity/2")))

        .singleCategory(c -> c.id(URI.create("https://example.com/activity/3")))

        .singleOther(o -> o.id(URI.create("https://example.com/activity/4")))

        .build();

    // When Serializing Attachment
    final JsonNode result =
        objectMapper.readTree(objectMapper.writeValueAsString(contextActivities));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(objectMapper
        .readTree(ResourceUtils.getFile("classpath:context_activities/context_activities.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final ContextActivities contextActivities = objectMapper.readValue(
        ResourceUtils.getFile("classpath:context_activities/context_activities.json"),
        ContextActivities.class);

    // When Calling ToString
    final String result = contextActivities.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "ContextActivities(parent=[Activity(objectType=ACTIVITY, id=https://example.com/activity/1, definition=null)], grouping=[Activity(objectType=ACTIVITY, id=https://example.com/activity/2, definition=null)], category=[Activity(objectType=ACTIVITY, id=https://example.com/activity/3, definition=null)], other=[Activity(objectType=ACTIVITY, id=https://example.com/activity/4, definition=null)])"));

  }

  @Test
  void whenBuildingContextActivitiesWithTwoParentsThenParentIsArrayWithSizeTwo()
      throws IOException {

    // When Building ContextActivities With Two Parents
    final ContextActivities contextActivities = ContextActivities.builder()

        .singleParent(p -> p.id(URI.create("https://example.com/activity/1")))

        .singleParent(p -> p.id(URI.create("https://example.com/activity/2")))

        .build();

    // Then Parent Is Array With Size Two
    assertThat(contextActivities.getParent(), arrayWithSize(2));

  }

  @Test
  void whenBuildingContextActivitiesWithTwoGroupingThenGroupingIsArrayWithSizeTwo()
      throws IOException {

    // When Building ContextActivities With Two Groupings
    final ContextActivities contextActivities = ContextActivities.builder()

        .singleGrouping(p -> p.id(URI.create("https://example.com/activity/1")))

        .singleGrouping(p -> p.id(URI.create("https://example.com/activity/2")))

        .build();

    // Then Grouping Is Array With Size Two
    assertThat(contextActivities.getGrouping(), arrayWithSize(2));

  }

  @Test
  void whenBuildingContextActivitiesWithTwoOtherThenOtherIsArrayWithSizeTwo() throws IOException {

    // When Building ContextActivities With Two Others
    final ContextActivities contextActivities = ContextActivities.builder()

        .singleOther(p -> p.id(URI.create("https://example.com/activity/1")))

        .singleOther(p -> p.id(URI.create("https://example.com/activity/2")))

        .build();

    // Then Other Is Array With Size Two
    assertThat(contextActivities.getOther(), arrayWithSize(2));

  }

  @Test
  void whenBuildingContextActivitiesWithTwoCategoriesThenCategoryIsArrayWithSizeTwo()
      throws IOException {

    // When Building ContextActivities With Two Categories
    final ContextActivities contextActivities = ContextActivities.builder()

        .singleCategory(p -> p.id(URI.create("https://example.com/activity/1")))

        .singleCategory(p -> p.id(URI.create("https://example.com/activity/2")))

        .build();

    // Then Category Is Array With Size Two
    assertThat(contextActivities.getCategory(), arrayWithSize(2));

  }

  @Test
  void whenBuildingContextActivitiesWithTwoParentsWithSameIdThenParentIsArrayWithSizeTwo()
      throws IOException {

    // When Building ContextActivities With Two Parents With Same Id
    final ContextActivities contextActivities = ContextActivities.builder()

        .singleParent(p -> p.id(URI.create("https://example.com/activity/1")))

        .singleParent(p -> p.id(URI.create("https://example.com/activity/1")))

        .build();

    // Then Parent Is Array With Size Two
    assertThat(contextActivities.getParent(), arrayWithSize(2));

  }

}
