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
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Attachment Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("Attachment tests")
class AttachmentTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void whenDeserializingActivityDefinitionThenResultIsInstanceOfAttachment() throws Exception {

    final File file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing ActivityDefinition
    final Attachment result = objectMapper.readValue(file, Attachment.class);

    // Then Result Is Instance Of Attachment
    assertThat(result, instanceOf(Attachment.class));

  }

  @Test
  void whenDeserializingActivityDefinitionThenUsageTypeIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing ActivityDefinition
    final Attachment result = objectMapper.readValue(file, Attachment.class);

    // Then UsageType Is Expected
    assertThat(result.getUsageType(),
        is(URI.create("http://adlnet.gov/expapi/attachments/signature")));

  }

  @Test
  void whenDeserializingActivityDefinitionThenDisplayIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing ActivityDefinition
    final Attachment result = objectMapper.readValue(file, Attachment.class);

    // Then Display Is Expected
    assertThat(result.getDisplay().get(Locale.US), is("Signature"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenDescriptionIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing ActivityDefinition
    final Attachment result = objectMapper.readValue(file, Attachment.class);

    // Then Description Is Expected
    assertThat(result.getDescription().get(Locale.US), is("A test signature"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenContentTypeIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing ActivityDefinition
    final Attachment result = objectMapper.readValue(file, Attachment.class);

    // Then ContentType Is Expected
    assertThat(result.getContentType(), is("application/octet-stream"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenLengthIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing ActivityDefinition
    final Attachment result = objectMapper.readValue(file, Attachment.class);

    // Then Length Is Expected
    assertThat(result.getLength(), is(4235));

  }

  @Test
  void whenDeserializingActivityDefinitionThenSha2IsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing ActivityDefinition
    final Attachment result = objectMapper.readValue(file, Attachment.class);

    // Then Sha2 Is Expected
    assertThat(result.getSha2(),
        is("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634"));

  }

  @Test
  void whenDeserializingActivityDefinitionThenFileUrlIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing ActivityDefinition
    final Attachment result = objectMapper.readValue(file, Attachment.class);

    // Then FileUrl Is Expected
    assertThat(result.getFileUrl(), is(URI.create("https://example.com")));

  }

  @Test
  void whenSerializingAttachmentThenResultIsEqualToExpectedJson() throws IOException {

    final Attachment attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .singleDisplay(Locale.US, "Signature")

        .singleDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Serializing Attachment
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(attachment));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(objectMapper.writeValueAsString(objectMapper.readValue(
            ResourceUtils.getFile("classpath:attachment/attachment.json"), Attachment.class)))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final Attachment attachment = objectMapper
        .readValue(ResourceUtils.getFile("classpath:attachment/attachment.json"), Attachment.class);

    // When Calling ToString
    final String result = attachment.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "Attachment(usageType=http://adlnet.gov/expapi/attachments/signature, display={en_US=Signature}, description={en_US=A test signature}, contentType=application/octet-stream, length=4235, sha2=672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634, fileUrl=https://example.com)"));

  }

  @Test
  public void whenValidatingAttachmentWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {


    final Attachment attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .singleDisplay(Locale.US, "Signature")

        .singleDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Validating Attachment With All Required Properties
    final Set<ConstraintViolation<Attachment>> constraintViolations =
        validator.validate(attachment);

    // Then ConstraintViolations Size Is Zero
    assertThat(constraintViolations, hasSize(0));

  }

  @Test
  public void whenValidatingAttachmentWithoutUsageTypeThenConstraintViolationsSizeIsOne() {

    final Attachment attachment = Attachment.builder()

        .singleDisplay(Locale.US, "Signature")

        .singleDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Validating Attachment Without UsageType
    final Set<ConstraintViolation<Attachment>> constraintViolations =
        validator.validate(attachment);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  public void whenValidatingAttachmentWithoutDisplayThenConstraintViolationsSizeIsOne() {


    final Attachment attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .singleDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Validating Attachment Without Display
    final Set<ConstraintViolation<Attachment>> constraintViolations =
        validator.validate(attachment);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  public void whenValidatingAttachmentWithoutContentTypeThenConstraintViolationsSizeIsOne() {

    final Attachment attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .singleDisplay(Locale.US, "Signature")

        .singleDescription(Locale.US, "A test signature")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Validating Attachment Without ContentType
    final Set<ConstraintViolation<Attachment>> constraintViolations =
        validator.validate(attachment);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  public void whenValidatingAttachmentWithoutSha2ThenConstraintViolationsSizeIsOne() {

    final Attachment attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .singleDisplay(Locale.US, "Signature")

        .singleDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Validating Attachment Without Sha2
    final Set<ConstraintViolation<Attachment>> constraintViolations =
        validator.validate(attachment);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  public void whenValidatingAttachmentWithoutLengthThenConstraintViolationsSizeIsOne() {

    final Attachment attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .singleDisplay(Locale.US, "Signature")

        .singleDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Validating Attachment Without Length
    final Set<ConstraintViolation<Attachment>> constraintViolations =
        validator.validate(attachment);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

}
