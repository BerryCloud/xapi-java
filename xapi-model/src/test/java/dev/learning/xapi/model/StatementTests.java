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
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
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

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Result Is Instance Of Statement
    assertThat(result, instanceOf(Statement.class));

  }

  @Test
  void whenDeserializingStatementWithIdThenIdIsExpected() throws IOException {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Id
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Id Is Expected
    assertThat(result.getId(), is(UUID.fromString("4b9175ba-367d-4b93-990b-34d4180039f1")));

  }

  @Test
  void whenDeserializingStatementWithActorThenActorIsInstanceOfActor() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Actor
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Actor Is Instance Of Actor
    assertThat(result.getActor(), instanceOf(Actor.class));

  }

  @Test
  void whenDeserializingStatementWithVerbThenVerbIsInstanceOfVerb() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Verb
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Verb Is Instance Of Verb
    assertThat(result.getVerb(), instanceOf(Verb.class));

  }

  @Test
  void whenDeserializingStatementWithObjectThenObjectIsInstanceOfStatementObject()
      throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Object
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Object Is Instance Of StatementObject
    assertThat(result.getObject(), instanceOf(StatementObject.class));

  }

  @Test
  void whenDeserializingStatementWithResultThenResultIsInstanceOfResult() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Result
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Result Is Instance Of Result
    assertThat(result.getResult(), instanceOf(Result.class));

  }

  @Test
  void whenDeserializingStatementWithContextThenContextIsInstanceOfContext() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Context
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Context Is Instance Of Context
    assertThat(result.getContext(), instanceOf(Context.class));

  }

  @Test
  void whenDeserializingStatementWithTimestampThenTimestampIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Timestamp
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Timestamp Is Expected
    assertThat(result.getTimestamp().toString(), is("2013-05-18T05:32:34.804Z"));

  }

  @Test
  void whenDeserializingStatementWithStoredThenStoredIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Stored
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Stored Is Expected
    assertThat(result.getStored().toString(), is("2013-05-18T05:32:34.804Z"));

  }

  @Test
  void whenDeserializedStatementWithAuthorityThenAuthorityIsInstanceOfActor() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserialized Statement With Authority
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Authority Is Instance Of Actor
    assertThat(result.getAuthority(), instanceOf(Actor.class));

  }

  @Test
  void whenDeserializingStatementWithVersionThenVersionIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Version
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Version Is Expected
    assertThat(result.getVersion(), is("1.0.0"));

  }

  @Test
  void whenDeserializingStatementWithAttachmentsThenAttachmentsIsInstanceOfAttachment()
      throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement/statement.json");

    // When Deserializing Statement With Attachments
    final var result = objectMapper.readValue(file, Statement.class);

    // Then Attachments Is Instance Of Attachment
    assertThat(result.getAttachments().get(0), instanceOf(Attachment.class));

  }

  @Test
  void whenSerializingStatementWithAgentWithAccountThenResultIsEqualToExpectedJson()
      throws IOException {

    final var statement = Statement.builder()

        .actor(a -> a.account(
            acc -> acc.name("A N Other").homePage(URI.create("https://example.com/account/1234"))))

        .verb(Verb.EXPERIENCED)

        .activityObject(o -> o.id("https://example.com/xapi/activity/simplestatement"))

        .build();

    // when Serializing Statement With Agent With Account
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(statement));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(objectMapper.writeValueAsString(objectMapper.readValue(
            ResourceUtils.getFile("classpath:statement/statement_with_agent_with_account.json"),
            Statement.class)))));

  }

  @Test
  void whenSerializingStatementThenResultIsEqualToExpectedJson() throws IOException {

    final var extensions = new LinkedHashMap<URI, Object>();
    extensions.put(URI.create("http://name"), "Kilby");


    final var attachment = Attachment.builder().usageType(URI.create("http://example.com"))
        .fileUrl(URI.create("http://example.com"))

        .addDisplay(Locale.US, "value")

        .addDescription(Locale.US, "value")

        .length(123)

        .sha2("123")

        .contentType("file")

        .build();

    final var account = Account.builder()

        .homePage(URI.create("https://example.com"))

        .name("13936749")

        .build();


    final var statement = Statement.builder()

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

            .language(Locale.US)

            .statementReference(s -> s.id(UUID.fromString("6690e6c9-3ef0-4ed3-8b37-7f3964730bee")))

        )

        .timestamp(Instant.parse("2013-05-18T05:32:34.804+00:00"))

        .stored(Instant.parse("2013-05-18T05:32:34.804+00:00"))

        .agentAuthority(a -> a.account(account))

        .activityObject(a -> a.id("http://www.example.com/meetings/occurances/34534")

            .definition(d -> d.addName(Locale.US,
                "A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object.")

                .addDescription(Locale.US,
                    "A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object.")

                .type(URI.create("http://adlnet.gov/expapi/activities/meeting"))

                .moreInfo(URI.create("http://virtualmeeting.example.com/345256"))

                .extensions(extensions)))

        .attachments(Collections.singletonList(attachment))

        .version("1.0.0")

        .build();

    // When Serializing Statement
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(statement));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(objectMapper.writeValueAsString(objectMapper.readValue(
            ResourceUtils.getFile("classpath:statement/statement.json"), Statement.class)))));

  }

  @Test
  void whenSerializingStatementWithEnLocaleThenResultIsEqualToExpectedJson() throws IOException {

    final var extensions = new LinkedHashMap<URI, Object>();
    extensions.put(URI.create("http://name"), "Kilby");

    final var attachment = Attachment.builder().usageType(URI.create("http://example.com"))
        .fileUrl(URI.create("http://example.com"))

        .addDisplay(Locale.ENGLISH, "value")

        .addDescription(Locale.ENGLISH, "value")

        .length(123)

        .sha2("123")

        .contentType("file")

        .build();

    final var account = Account.builder()

        .homePage(URI.create("https://example.com"))

        .name("13936749")

        .build();


    final var statement = Statement.builder()

        .id(UUID.fromString("4b9175ba-367d-4b93-990b-34d4180039f1"))

        .actor(a -> a.name("A N Other"))

        .verb(v -> v.id(URI.create("http://example.com/xapi/verbs#sent-a-statement"))
            .addDisplay(Locale.ENGLISH, "attended"))

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

            .definition(d -> d.addName(Locale.ENGLISH,
                "A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object.")

                .addDescription(Locale.ENGLISH,
                    "A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the verb, or the Activity/object.")

                .type(URI.create("http://adlnet.gov/expapi/activities/meeting"))

                .moreInfo(URI.create("http://virtualmeeting.example.com/345256"))

                .extensions(extensions)))

        .attachments(Collections.singletonList(attachment))

        .version("1.0.0")

        .build();

    // When Serializing Statement With En Locale
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(statement));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(objectMapper.writeValueAsString(objectMapper.readValue(
            ResourceUtils.getFile("classpath:statement/statement_with_en_locale.json"),
            Statement.class)))));

  }

  @Test
  void givenStatementWithPassedVerbWhenCallingToBuilderAndSettingVerbToCompletedThenResultVerbIsCompleted() {

    // Given Statement With Passed Verb
    final var passed = Statement.builder()

        .actor(a -> a.name("A N Other"))

        .verb(Verb.PASSED)

        .activityObject(a -> a.id("https://example.com/activity/simplestatement"))

        .build();


    // When Calling ToBuilder And Setting Verb To Completed
    final var result = passed.toBuilder()

        .verb(Verb.COMPLETED)

        .build();

    // Then Result Verb Is Completed
    assertThat(result.getVerb(), is(Verb.COMPLETED));

  }

  @Test
  void WhenCallingToBuilderAndSettingVerbToCompletedThenResultVerbIsCompleted() {

    // When Building Statement With Statement Reference Object
    final var statement = Statement.builder()

        .actor(a -> a.name("A N Other"))

        .verb(Verb.VOIDED)

        .statementReferenceObject(
            sr -> sr.id(UUID.fromString("c972020f-0718-4033-95d0-4b502a115aa9")))

        .build();

    // Then Statement Object Is Instances Of StatementReference
    assertThat(statement.getObject(), instanceOf(StatementReference.class));

  }

  @Test
  void whenValidatingStatementWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {

    final var statement = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

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

    final var statement = Statement.builder()

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

    final var statement = Statement.builder()

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

    final var statement = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.EXPERIENCED)

        .build();

    // When Validating Statement Without An Activity
    final Set<ConstraintViolation<Statement>> constraintViolations = validator.validate(statement);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  void whenValidatingStatementWithSubStatementWithStatementReferenceThenConstraintViolationsSizeIsZero() {

    final var statementRef = StatementReference.builder()
        .id(UUID.fromString("9e13cefd-53d3-4eac-b5ed-2cf6693903bb")).build();

    final var subStatement = SubStatement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.EXPERIENCED)

        .object(statementRef)

        .build();

    final var statement = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.EXPERIENCED)

        .object(subStatement)

        .build();

    // When Validating Statement With SubStatement With StatementReference
    final Set<ConstraintViolation<Statement>> constraintViolations = validator.validate(statement);

    // Then ConstraintViolations Size Is Zero
    assertThat(constraintViolations, hasSize(0));

  }

  @Test
  void whenBuildingStatementWithTwoAttachmentsThenAttachmentsHasTwoEntries() {

    // When Building Statement With Two Attachments
    final var extensions = new LinkedHashMap<URI, Object>();
    extensions.put(URI.create("http://name"), "Kilby");

    final var attachment = Attachment.builder().usageType(URI.create("http://example.com"))
        .fileUrl(URI.create("http://example.com"))

        .addDisplay(Locale.ENGLISH, "value")

        .addDescription(Locale.ENGLISH, "value")

        .length(123)

        .sha2("123")

        .contentType("file")

        .build();

    final var account = Account.builder()

        .homePage(URI.create("https://example.com"))

        .name("13936749")

        .build();


    final var statement = Statement.builder()

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

        .addAttachment(attachment)

        .addAttachment(a -> a.usageType(URI.create("http://example.com"))

            .fileUrl(URI.create("http://example.com/2"))

            .addDisplay(Locale.ENGLISH, "value2")

            .addDescription(Locale.ENGLISH, "value2")

            .length(1234)

            .sha2("1234")

            .contentType("file")

        )

        .version("1.0.0")

        .build();

    // Then Attachments Has Two Entries
    assertThat(statement.getAttachments(), hasSize(2));

  }

  @Test
  void whenDeserializingStatementWithActorWithoutObjectTypeThenNoExceptionIsThrown()
      throws IOException {

    Assertions.assertDoesNotThrow(() -> {
      objectMapper.readValue("""
          {
            "actor":{
              "name":"xAPI mbox",
              "mbox":"mailto:xapi@adlnet.gov"
            },
            "verb":{
              "id":"http://adlnet.gov/expapi/verbs/attended",
              "display":{
                "en-GB":"attended",
                "en_US":"attended"
              }
            },
            "object":{
              "objectType":"Activity",
              "id":"http://www.example.com/meetings/occurances/34534"
            }
          }""", Statement.class);
    });

  }

}
