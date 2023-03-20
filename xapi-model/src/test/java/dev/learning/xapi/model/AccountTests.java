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
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Account Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 * @author Thomas Turrell-Croft
 */
@DisplayName("Account tests")
class AccountTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void whenDeserializingAccountThenResultIsInstanceOfAccount() throws Exception {

    final File file = ResourceUtils.getFile("classpath:account/account.json");

    // When Deserializing Account
    final Account result = objectMapper.readValue(file, Account.class);

    // Then Result Is Instance Of Account
    assertThat(result, instanceOf(Account.class));

  }

  @Test
  void whenDeserializingAccountThenHomePageIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:account/account.json");

    // When Deserializing Account
    final Account result = objectMapper.readValue(file, Account.class);

    // Then HomePage Is Expected
    assertThat(result.getHomePage().toString(), is("https://www.example.com"));

  }

  @Test
  void whenDeserializingAccountThenNameIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:account/account.json");

    // When Deserializing Account
    final Account result = objectMapper.readValue(file, Account.class);

    // Then Name Is Expected
    assertThat(result.getName(), is("Example"));

  }

  @Test
  void whenSerializingAccountThenResultIsEqualToExpectedJson() throws IOException {

    final Account account = Account.builder()

        .name("Example")

        .homePage(URI.create("https://www.example.com"))

        .build();

    // When Serializing Account
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(account));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:account/account.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final Account account = Account.builder()

        .name("Example")

        .homePage(URI.create("https://www.example.com"))

        .build();

    // When Calling ToString
    final String result = account.toString();

    // Then Result Is Expected
    assertThat(result, is("Account(homePage=https://www.example.com, name=Example)"));

  }

  @Test
  void whenValidatingAccountWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {


    final Account account = Account.builder()

        .name("Example")

        .homePage(URI.create("https://www.example.com"))

        .build();

    // When Validating Account With All Required Properties
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is Zero
    assertThat(constraintViolations, hasSize(0));

  }

  @Test
  void whenValidatingAccountWithoutNameThenConstraintViolationsSizeIsOne() {

    final Account account = Account.builder()

        .homePage(URI.create("https://www.example.com"))

        .build();

    // When Validating Account Without Name
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  void whenValidatingAccountWithoutHomepageThenConstraintViolationsSizeIsOne() {

    final Account account = Account.builder()

        .name("Example")

        .build();

    // When Validating Account Without Homepage
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

  @Test
  void whenValidatingAccountWithEmptyNameThenConstraintViolationsSizeIsOne() {

    final Account account = Account.builder()

        .name("")

        .homePage(URI.create("https://www.example.com"))

        .build();

    // When Validating Account With Empty Name
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

}
