/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Context Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("Context tests")
class ContextTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenDeserializingContextThenResultIsInstanceOfContext() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Result Is Instance Of Context
    assertThat(result, instanceOf(Context.class));

  }

  @Test
  void whenDeserializingContextThenRegistrationIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Registration Is Expected
    assertThat(result.getRegistration(),
        is(UUID.fromString("1d527164-ed0d-4b1d-9f9b-39aab0e4a089")));

  }

  @Test
  void whenDeserializingContextThenRevisionIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Revision Is Expected
    assertThat(result.getRevision(), is("revision"));

  }

  @Test
  void whenDeserializingContextThenPlatformIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Platform Is Expected
    assertThat(result.getPlatform(), is("platform"));

  }

  @Test
  void whenDeserializingContextThenLanguageIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Language Is Expected
    assertThat(result.getLanguage(), is(Locale.ENGLISH));

  }

  @Test
  void whenDeserializingContextThenInstructorIsInstanceOfActor() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Instructor Is Instance Of Actor
    assertThat(result.getInstructor(), instanceOf(Actor.class));

  }

  @Test
  void whenDeserializingContextThenTeamIsInstanceOfGroup() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Team Is Instance Of Group
    assertThat(result.getTeam(), instanceOf(Group.class));

  }

  @Test
  void whenDeserializingContextThenStatementIsInstanceOfStatementReference() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Statement Is Instance Of StatementReference
    assertThat(result.getStatement(), instanceOf(StatementReference.class));

  }

  @Test
  void whenDeserializingContextThenContextActivitiesIsInstanceOfContextActivities()
      throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then ContextActivities Is Instance Of ContextActivities
    assertThat(result.getContextActivities(), instanceOf(ContextActivities.class));

  }

  @Test
  void whenDeserializingContextThenExtensionsAreExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context.json");

    // When Deserializing Context
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Extensions Are Expected
    assertThat(result.getExtensions().get(URI.create("http://url")), is("www.example.com"));
  }

  @Test
  void whenDeserializingContextWithoutRegistrationThenRegistrationIsNull() throws Exception {

    final File file = ResourceUtils.getFile("classpath:context/context_without_registration.json");

    // When Deserializing Context Without Registration
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Registration Is Null
    assertThat(result.getRegistration(), is(nullValue()));
  }

  @Test
  void whenDeserializingContextWithEmptyRegistrationThenExtensionsAreExpected() throws Exception {

    final File file =
        ResourceUtils.getFile("classpath:context/context_with_empty_registration.json");

    // When Deserializing Context With Empty Registration
    final Context result = objectMapper.readValue(file, Context.class);

    // Then Registration Is Null
    assertThat(result.getRegistration(), is(nullValue()));
  }

  @Test
  void whenSerializingContextWithGroupInstructorThenResultIsEqualToExpectedJson()
      throws IOException {

    final Agent leader1 =
        Agent.builder().name("Leader 1").mbox("mailto:leader1@example.com").build();

    final Agent leader2 =
        Agent.builder().name("Leader 2").mbox("mailto:leader2@example.com").build();

    final Context context = Context.builder()

        .groupInstructor(g -> g.addMember(leader1).addMember(leader2))

        .build();

    // When Serializing Context With Group Instructor
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(context));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(objectMapper
        .readTree(ResourceUtils.getFile("classpath:context/context_with_group_instructor.json"))));

  }

  @Test
  void whenSerializingContextThenResultIsEqualToExpectedJson() throws IOException {

    final LinkedHashMap<URI, Object> extensions = new LinkedHashMap<>();
    extensions.put(URI.create("http://url"), "www.example.com");

    final Context context = Context.builder()

        .registration(UUID.fromString("1d527164-ed0d-4b1d-9f9b-39aab0e4a089"))

        .agentInstructor(i -> i.name("Andrew Downes").mbox("mailto:andrew@example.co.uk"))

        .team(t -> t.name("Example Group"))

        .revision("revision")

        .platform("platform")

        .language(Locale.ENGLISH)

        .statementReference(s -> s.id(UUID.fromString("e9b6b9ed-ef48-4986-9b86-2ef697578bf7")))

        .contextActivities(ca -> ca

            .addParent(p -> p.id(URI.create("https://example.com/activity/1")))

            .addGrouping(g -> g.id(URI.create("https://example.com/activity/2")))

            .addCategory(c -> c.id(URI.create("https://example.com/activity/3")))

            .addOther(o -> o.id(URI.create("https://example.com/activity/4"))))

        .extensions(extensions)

        .build();

    // When Serializing Context
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(context));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:context/context.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final Context context = objectMapper
        .readValue(ResourceUtils.getFile("classpath:context/context.json"), Context.class);

    // When Calling ToString
    final String result = context.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "Context(registration=1d527164-ed0d-4b1d-9f9b-39aab0e4a089, instructor=Agent(super=Actor(name=Andrew Downes, mbox=mailto:andrew@example.co.uk, mboxSha1sum=null, openid=null, account=null), objectType=null), team=Group(super=Actor(name=Example Group, mbox=null, mboxSha1sum=null, openid=null, account=null), objectType=GROUP, member=null), contextActivities=ContextActivities(parent=[Activity(objectType=ACTIVITY, id=https://example.com/activity/1, definition=null)], grouping=[Activity(objectType=ACTIVITY, id=https://example.com/activity/2, definition=null)], category=[Activity(objectType=ACTIVITY, id=https://example.com/activity/3, definition=null)], other=[Activity(objectType=ACTIVITY, id=https://example.com/activity/4, definition=null)]), revision=revision, platform=platform, language=en, statement=StatementReference(objectType=STATEMENTREF, id=e9b6b9ed-ef48-4986-9b86-2ef697578bf7), extensions={http://url=www.example.com})"));

  }

}
