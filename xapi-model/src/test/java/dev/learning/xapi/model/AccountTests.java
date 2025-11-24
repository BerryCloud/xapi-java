/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import tools.jackson.databind.ObjectMapper;

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

    final var file = ResourceUtils.getFile("classpath:account/account.json");

    // When Deserializing Account
    final var result = objectMapper.readValue(file, Account.class);

    // Then Result Is Instance Of Account
    assertThat(result, instanceOf(Account.class));
  }

  @Test
  void whenDeserializingAccountThenHomePageIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:account/account.json");

    // When Deserializing Account
    final var result = objectMapper.readValue(file, Account.class);

    // Then HomePage Is Expected
    assertThat(result.getHomePage().toString(), is("https://www.example.com"));
  }

  @Test
  void whenDeserializingAccountThenNameIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:account/account.json");

    // When Deserializing Account
    final var result = objectMapper.readValue(file, Account.class);

    // Then Name Is Expected
    assertThat(result.getName(), is("Example"));
  }

  @Test
  void whenSerializingAccountThenResultIsEqualToExpectedJson() throws IOException {

    final var account =
        Account.builder().name("Example").homePage(URI.create("https://www.example.com")).build();

    // When Serializing Account
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(account));

    // Then Result Is Equal To Expected Json
    assertThat(
        result, is(objectMapper.readTree(ResourceUtils.getFile("classpath:account/account.json"))));
  }

  @Test
  void whenCallingToStringThenResultIsExpected() {

    final var account =
        Account.builder().name("Example").homePage(URI.create("https://www.example.com")).build();

    // When Calling ToString
    final var result = account.toString();

    // Then Result Is Expected
    assertThat(result, is("Account(homePage=https://www.example.com, name=Example)"));
  }

  @Test
  void whenValidatingAccountWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {

    final var account =
        Account.builder().name("Example").homePage(URI.create("https://www.example.com")).build();

    // When Validating Account With All Required Properties
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is Zero
    assertThat(constraintViolations, hasSize(0));
  }

  @Test
  void whenValidatingAccountWithoutNameThenConstraintViolationsSizeIsOne() {

    final var account = Account.builder().homePage(URI.create("https://www.example.com")).build();

    // When Validating Account Without Name
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));
  }

  @Test
  void whenValidatingAccountWithoutHomepageThenConstraintViolationsSizeIsOne() {

    final var account = Account.builder().name("Example").build();

    // When Validating Account Without Homepage
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));
  }

  @Test
  void whenValidatingAccountWithEmptyNameThenConstraintViolationsSizeIsOne() {

    final var account =
        Account.builder().name("").homePage(URI.create("https://www.example.com")).build();

    // When Validating Account With Empty Name
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));
  }

  @Test
  void whenValidatingAccountWithHomepageWithNoSchemeThenConstraintViolationsSizeIsOne() {

    final var account =
        Account.builder().name("Example").homePage(URI.create("www.example.com")).build();

    // When Validating Account With Homepage With No Scheme
    final Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));
  }
}
