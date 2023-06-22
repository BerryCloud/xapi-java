/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@DisplayName("Attachment tests")
class AttachmentTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void whenDeserializingAttachmentThenResultIsInstanceOfAttachment() throws Exception {

    final var file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing Attachment
    final var result = objectMapper.readValue(file, Attachment.class);

    // Then Result Is Instance Of Attachment
    assertThat(result, instanceOf(Attachment.class));

  }

  @Test
  void whenDeserializingAttachmentThenUsageTypeIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing Attachment
    final var result = objectMapper.readValue(file, Attachment.class);

    // Then UsageType Is Expected
    assertThat(result.getUsageType(),
        is(URI.create("http://adlnet.gov/expapi/attachments/signature")));

  }

  @Test
  void whenDeserializingAttachmentThenDisplayIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing Attachment
    final var result = objectMapper.readValue(file, Attachment.class);

    // Then Display Is Expected
    assertThat(result.getDisplay().get(Locale.US), is("Signature"));

  }

  @Test
  void whenDeserializingAttachmentThenDescriptionIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing Attachment
    final var result = objectMapper.readValue(file, Attachment.class);

    // Then Description Is Expected
    assertThat(result.getDescription().get(Locale.US), is("A test signature"));

  }

  @Test
  void whenDeserializingAttachmentThenContentTypeIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing Attachment
    final var result = objectMapper.readValue(file, Attachment.class);

    // Then ContentType Is Expected
    assertThat(result.getContentType(), is("application/octet-stream"));

  }

  @Test
  void whenDeserializingAttachmentThenLengthIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing Attachment
    final var result = objectMapper.readValue(file, Attachment.class);

    // Then Length Is Expected
    assertThat(result.getLength(), is(4235));

  }

  @Test
  void whenDeserializingAttachmentThenSha2IsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing Attachment
    final var result = objectMapper.readValue(file, Attachment.class);

    // Then Sha2 Is Expected
    assertThat(result.getSha2(),
        is("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634"));

  }

  @Test
  void whenDeserializingAttachmentThenFileUrlIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:attachment/attachment.json");

    // When Deserializing Attachment
    final var result = objectMapper.readValue(file, Attachment.class);

    // Then FileUrl Is Expected
    assertThat(result.getFileUrl(), is(URI.create("https://example.com")));

  }

  @Test
  void whenSerializingAttachmentThenResultIsEqualToExpectedJson() throws IOException {

    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Serializing Attachment
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(attachment));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(objectMapper.writeValueAsString(objectMapper.readValue(
            ResourceUtils.getFile("classpath:attachment/attachment.json"), Attachment.class)))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final var attachment = objectMapper
        .readValue(ResourceUtils.getFile("classpath:attachment/attachment.json"), Attachment.class);

    // When Calling ToString
    final var result = attachment.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "Attachment(usageType=http://adlnet.gov/expapi/attachments/signature, display={en_US=Signature}, description={en_US=A test signature}, contentType=application/octet-stream, length=4235, sha2=672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634, fileUrl=https://example.com, content=null)"));

  }

  /*
   * Builder Tests
   */

  @Test
  void whenBuildingAttachmentWithDataThenDataIsSet() {

    // When Building Attachment With Data
    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))

        .addDisplay(Locale.US, "Text")

        .contentType("plain/text")

        .length(4)

        .content("text")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // Then Data Is Set
    assertThat(new String(attachment.getContent(), StandardCharsets.UTF_8), is("text"));

  }

  @Test
  void givenAttachmentWithStringDataWhenGettingSHA2ThenResultIsExpected() {

    // Given Attachment With String Data
    final var attachment = Attachment.builder()

        .content("Simple attachment").length(17)

        .contentType("text/plain")

        .usageType(URI.create("https://example.com/attachments/greeting"))

        .addDisplay(Locale.ENGLISH, "text attachment")

        .build();

    // When Getting SHA2
    final var result = attachment.getSha2();

    // Then Result Is Expected
    assertThat(result, is("b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5"));

  }

  @Test
  void givenAttachmentWithBinaryDataWhenGettingSHA2ThenResultIsExpected()
      throws FileNotFoundException, IOException {

    final var data =
        Files.readAllBytes(ResourceUtils.getFile("classpath:attachment/example.jpg").toPath());

    // Given Attachment With Binary Data
    final var attachment = Attachment.builder()

        .content(data).length(data.length)

        .contentType("image/jpeg")

        .usageType(URI.create("https://example.com/attachments/greeting"))

        .addDisplay(Locale.ENGLISH, "JPEG attachment")

        .build();

    // When Getting SHA2
    final var result = attachment.getSha2();

    // Then Result Is Expected
    assertThat(result, is("27c7a7c1e3d2ff43e4ee1a8915fef351d1ef75d5aeff873e9b2893f4589dcdcc"));

  }

  @Test
  void whenBuildingAttachmentWithDataAndSha2ThenSha2IsTheCalculatedOne() {

    // When Building Attachment With Data And Sha2
    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))

        .addDisplay(Locale.US, "Text")

        .contentType("plain/text")

        .length(4)

        .content("text")

        .sha2("000000000000000000000000000000000000000000000")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // Then Sha2 Is Set Is The Calculated One
    assertThat(attachment.getSha2(),
        is("982d9e3eb996f559e633f4d194def3761d909f5a3b647d1a851fead67c32c9d1"));

  }

  @Test
  void whenBuildingAttachmentWithNullByteArrayContentThenSha2IsNull() {

    // When Building Attachment With Null Byte Array Content
    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))

        .addDisplay(Locale.US, "Text")

        .contentType("plain/text")

        .length(4)

        .content((byte[]) null)

        .fileUrl(URI.create("https://example.com"))

        .build();

    // Then Sha2 Is Null
    assertNull(attachment.getSha2());

  }

  @Test
  void whenBuildingAttachmentWithNullStringContentThenSha2IsNull() {

    // When Building Attachment With Null String Content
    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))

        .addDisplay(Locale.US, "Text")

        .contentType("plain/text")

        .length(4)

        .content((String) null)

        .fileUrl(URI.create("https://example.com"))

        .build();

    // Then Sha2 Is Null
    assertNull(attachment.getSha2());

  }

  @Test
  void whenBuildingAttachmentWithTwoDisplayValuesThenDisplayLanguageMapHasTwoEntries() {

    // When Building Attachment With Two Display Values
    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDisplay(Locale.US, "Signature")

        .addDisplay(Locale.GERMAN, "Unterschrift")

        .addDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // Then Display Language Map Has Two Entries
    assertThat(attachment.getDisplay(), aMapWithSize(2));

  }

  @Test
  void whenBuildingAttachmentWithTwoDescriptionValuesThenDisplayLanguageMapHasTwoEntries() {

    // When Building Attachment With Two Description Values
    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

        .addDescription(Locale.GERMAN, "Eine Testsignatur")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // Then Description Language Map Has Two Entries
    assertThat(attachment.getDescription(), aMapWithSize(2));

  }

  @Test
  void whenValidatingAttachmentWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {

    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

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
  void whenValidatingAttachmentWithoutUsageTypeThenConstraintViolationsSizeIsOne() {

    final var attachment = Attachment.builder()

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

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
  void whenValidatingAttachmentWithoutDisplayThenConstraintViolationsSizeIsOne() {


    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDescription(Locale.US, "A test signature")

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
  void whenValidatingAttachmentWithoutContentTypeThenConstraintViolationsSizeIsOne() {

    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

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
  void whenValidatingAttachmentWithoutSha2ThenConstraintViolationsSizeIsOne() {

    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

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
  void whenValidatingAttachmentWithoutLengthThenConstraintViolationsSizeIsOne() {

    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

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

  @Test
  void whenValidatingAttachmentWithUsageTypeWithoutSchemeThenConstraintViolationsSizeIsOne() {

    final var attachment = Attachment.builder()

        .usageType(URI.create("signature"))

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("https://example.com"))

        .build();

    // When Validating Attachment With UsageType Without Scheme
    final Set<ConstraintViolation<Attachment>> constraintViolations =
        validator.validate(attachment);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  void whenValidatingAttachmentWithFileUrlWithoutSchemeThenConstraintViolationsSizeIsOne() {

    final var attachment = Attachment.builder()

        .usageType(URI.create("http://adlnet.gov/expapi/attachments/signature"))

        .addDisplay(Locale.US, "Signature")

        .addDescription(Locale.US, "A test signature")

        .contentType("application/octet-stream")

        .length(4235)

        .sha2("672fa5fa658017f1b72d65036f13379c6ab05d4ab3b6664908d8acf0b6a0c634")

        .fileUrl(URI.create("example.com"))

        .build();

    // When Validating Attachment With FileUrl Without Scheme
    final Set<ConstraintViolation<Attachment>> constraintViolations =
        validator.validate(attachment);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

}
