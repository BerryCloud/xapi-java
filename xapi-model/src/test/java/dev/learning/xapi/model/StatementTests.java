/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
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
import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Statement Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 * @author Thomas Turrell-Croft
 */
@DisplayName("Statement tests")
class StatementTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  /*
   * Deserialization tests.
   */

  @Test
  void whenDeserializingStatementThenResultIsInstanceOfStatement() throws IOException {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Result Is Instance Of Statement
    assertThat(result, instanceOf(Statement.class));

  }

  @Test
  void whenDeserializingStatementWithIdThenIdIsExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Id
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Id Is Expected
    assertThat(result.getId(), is(UUID.fromString("4b9175ba-367d-4b93-990b-34d4180039f1")));

  }

  @Test
  void whenDeserializingStatementWithActorThenActorIsInstanceOfActor() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Actor
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Actor Is Instance Of Actor
    assertThat(result.getActor(), instanceOf(Actor.class));

  }

  @Test
  void whenDeserializingStatementWithVerbThenVerbIsInstanceOfVerb() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Verb
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Verb Is Instance Of Verb
    assertThat(result.getVerb(), instanceOf(Verb.class));

  }

  @Test
  void whenDeserializingStatementWithObjectThenObjectIsInstanceOfStatementObject()
      throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Object
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Object Is Instance Of StatementObject
    assertThat(result.getObject(), instanceOf(StatementObject.class));

  }

  @Test
  void whenDeserializingStatementWithResultThenResultIsInstanceOfResult() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Result
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Result Is Instance Of Result
    assertThat(result.getResult(), instanceOf(Result.class));

  }

  @Test
  void whenDeserializingStatementWithContextThenContextIsInstanceOfContext() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Context
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Context Is Instance Of Context
    assertThat(result.getContext(), instanceOf(Context.class));

  }

  @Test
  void whenDeserializingStatementWithTimestampThenTimestampIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Timestamp
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Timestamp Is Expected
    assertThat(result.getTimestamp().toString(), is("2013-05-18T05:32:34.804Z"));

  }

  @Test
  void whenDeserializingStatementWithStoredThenStoredIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Stored
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Stored Is Expected
    assertThat(result.getStored().toString(), is("2013-05-18T05:32:34.804Z"));

  }

  @Test
  void whenDeserializedStatementWithAuthorityThenAuthorityIsInstanceOfActor() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserialized Statement With Authority
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Authority Is Instance Of Actor
    assertThat(result.getAuthority(), instanceOf(Actor.class));

  }

  @Test
  void whenDeserializingStatementWithVersionThenVersionIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Version
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Version Is Expected
    assertThat(result.getVersion(), is("1.0.0"));

  }

  @Test
  void whenDeserializingStatementWithAttachmentsThenAttachmentsIsInstanceOfAttachment()
      throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Attachments
    final Statement result = objectMapper.readValue(file, Statement.class);

    // Then Attachments Is Instance Of Attachment
    assertThat(result.getAttachments()[0], instanceOf(Attachment.class));

  }

  @Test
  void whenSerializingStatementThenResultIsEqualToExpectedJson() throws IOException {

    final LinkedHashMap<URI, Object> extensions = new LinkedHashMap<>();
    extensions.put(URI.create("http://name"), "Kilby");


    final Attachment attachment = Attachment.builder().usageType(URI.create("http://example.com"))
        .fileUrl(URI.create("http://example.com"))

        .addDisplay(Locale.ENGLISH, "value")

        .addDescription(Locale.ENGLISH, "value")

        .length(123)

        .sha2("123")

        .contentType("file")

        .build();

    final Account account = Account.builder()

        .homePage(URI.create("https://example.com"))

        .name("13936749")

        .build();


    final Statement statement = Statement.builder()

        .id(UUID.fromString("4b9175ba-367d-4b93-990b-34d4180039f1"))

        .actor(a -> a.name("A N Other"))

        .verb(v -> v.id(URI.create("http://example.com/xapi/verbs#sent-a-statement"))
            .addDisplay(Locale.US, "attended"))

        .result(r -> r.success(true).completion(true).response("Response").duration("P1D"))

        .context(c -> c

            .registration(UUID.fromString("ec531277-b57b-4c15-8d91-d292c5b2b8f7"))

            .agentInstructor(a -> a.name("A N Other").account(account))

            .team(t -> t.name("Team").mbox("mailto:team@example.com"))

            .platform("Example virtual meeting software")

            .language(Locale.ENGLISH)

            .statementReference(s -> s.id(UUID.fromString("6690e6c9-3ef0-4ed3-8b37-7f3964730bee")))

        )

        .timestamp(Instant.parse("2013-05-18T05:32:34.804+00:00"))

        .stored(Instant.parse("2013-05-18T05:32:34.804+00:00"))

        .agentAuthority(a -> a.account(account))

        .activityObject(a -> a.id("http://www.example.com/meetings/occurances/34534")

            .definition(d -> d.addName(Locale.UK,
                "A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object.")

                .addDescription(Locale.UK,
                    "A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object.")

                .type(URI.create("http://adlnet.gov/expapi/activities/meeting"))

                .moreInfo(URI.create("http://virtualmeeting.example.com/345256"))

                .extensions(extensions)))

        .attachments(new Attachment[] {attachment})

        .version("1.0.0")

        .build();

    // When Serializing Statement
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(statement));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(objectMapper.writeValueAsString(objectMapper.readValue(
            ResourceUtils.getFile("classpath:statement/statement.json"), Statement.class)))));

  }

  @Test
  void givenStatementWithPassedVerbWhenCallingToBuilderAndSettingVerbToCompletedThenResultVerbIsCompleted() {

    // Given Statement With Passed Verb
    final Statement passed = Statement.builder()

        .actor(a -> a.name("A N Other"))

        .verb(Verb.PASSED)

        .activityObject(a -> a.id("https://example.com/activity/simplestatement"))

        .build();


    // When Calling ToBuilder And Setting Verb To Completed
    Statement result = passed.toBuilder()

        .verb(Verb.COMPLETED)

        .build();

    // Then Result Verb Is Completed
    assertThat(result.getVerb(), is(Verb.COMPLETED));

  }

  @Test
  void whenValidatingStatementWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {

    final Statement statement = Statement.builder()

        .actor(a -> a.name("A N Other").account(acc->acc.name("other").homePage(URI.create("https://example.com"))))

        .verb(Verb.EXPERIENCED)

        .activityObject(o -> o.id("https://example.com/xapi/activity/simplestatement"))

        .build();

    // When Validating Statement With All Required Properties
    final Set<ConstraintViolation<Statement>> constraintViolations = validator.validate(statement);

    // Then ConstraintViolations Size Is Zero
    assertThat(constraintViolations, hasSize(0));

  }


  @Test
  void whenValidatingStatementWithoutActorThenConstraintViolationsSizeIsOne() {

    final Statement statement = Statement.builder()

        .verb(Verb.EXPERIENCED)

        .activityObject(o -> o.id("https://example.com/xapi/activity/simplestatement"))

        .build();

    // When Validating Statement Without Actor
    final Set<ConstraintViolation<Statement>> constraintViolations = validator.validate(statement);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  void whenValidatingStatementWithoutAVerbThenConstraintViolationsSizeIsOne() {

    final Statement statement = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .activityObject(o -> o.id("https://example.com/xapi/activity/simplestatement"))

        .build();

    // When Validating Statement Without A Verb
    final Set<ConstraintViolation<Statement>> constraintViolations = validator.validate(statement);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  void whenValidatingStatementWithoutAnActivityThenConstraintViolationsSizeIsOne() {

    final Statement statement = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.EXPERIENCED)

        .build();

    // When Validating Statement Without An Activity
    final Set<ConstraintViolation<Statement>> constraintViolations = validator.validate(statement);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }


}
