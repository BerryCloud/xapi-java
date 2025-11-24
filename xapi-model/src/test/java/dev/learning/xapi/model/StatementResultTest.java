/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * StatementResult Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 * @author Thomas Turrell-Croft
 */
@DisplayName("StatementResult tests")
class StatementResultTest {

  final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenDeserializingStatementResultThenResultIsInstanceOfStatementResult() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement_result/statement_result.json");

    // When Deserializing StatementResult
    final var result = objectMapper.readValue(file, StatementResult.class);

    // Then Result Is Instance Of StatementResult
    assertThat(result, instanceOf(StatementResult.class));
  }

  @Test
  void whenDeserializingStatementResultThenStatementsIsInstanceOfStatement() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement_result/statement_result.json");

    // When Deserializing StatementResult
    final var result = objectMapper.readValue(file, StatementResult.class);

    // Then Statements Is Instance Of Statement
    assertThat(result.getStatements().get(0), instanceOf(Statement.class));
  }

  @Test
  void whenDeserializingStatementResultThenMoreIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:statement_result/statement_result.json");

    // When Deserializing StatementResult
    final var result = objectMapper.readValue(file, StatementResult.class);

    // Then More Is Expected
    assertThat(result.getMore(), is(URI.create("123")));
  }

  @Test
  void whenSerializingStatementResultThenResultIsEqualToExpectedJson() throws IOException {

    final var statementResult =
        StatementResult.builder()
            .addStatement(s -> s.id(UUID.fromString("fd41c918-b88b-4b20-a0a5-a4c32391aaa0")))
            .more(URI.create("123"))
            .build();

    // When Serializing StatementResult
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(statementResult));

    // Then Result Is Equal To Expected Json
    assertThat(
        result,
        is(
            objectMapper.readTree(
                "{\"statements\":[{\"id\":\"fd41c918-b88b-4b20-a0a5-a4c32391aaa0\"}],\"more\":\"123\"}")));
  }

  @Test
  void whenCallingToStringThenResultIsExpected() {

    final var statementResult =
        StatementResult.builder()
            .addStatement(
                s -> s.id(UUID.fromString("fd41c918-b88b-4b20-a0a5-a4c32391aaa0")).version("1.0.3"))
            .more(URI.create("123"))
            .build();

    // When Calling To String
    final var result = statementResult.toString();

    // Then Result Is Expected
    assertThat(
        result,
        is(
            "StatementResult(statements=[Statement(id=fd41c918-b88b-4b20-a0a5-a4c32391aaa0, actor=null, verb=null, object=null, result=null, context=null, timestamp=null, stored=null, authority=null, version=1.0.3, attachments=null)], more=123)"));
  }

  @Test
  void whenBuildingStatementResultWithTwoStatementsThenStatmentsIsArrayWithSizeTwo() {

    // When Building StatementResult With Two Statements
    final var statementResult =
        StatementResult.builder()
            .addStatement(s -> s.id(UUID.fromString("fd41c918-b88b-4b20-a0a5-a4c32391aaa0")))
            .addStatement(s -> s.id(UUID.fromString("0b05db78-5554-4cde-8673-56ca15580d1b")))
            .more(URI.create("123"))
            .build();

    // Then Statments Is Array With Size Two
    assertThat(statementResult.getStatements(), hasSize(2));
  }

  @Test
  void whenBuildingStatementResultWithEmptyMoreThenHasMoreIsFalse() {

    // When Building StatementResult With Empty More
    final var statementResult = StatementResult.builder().more(URI.create("")).build();

    // Then HasMore Is False
    assertThat(statementResult.hasMore(), is(false));
  }

  @Test
  void whenBuildingStatementResultWithNullMoreThenHasMoreIsFalse() {

    // When Building StatementResult With Null More
    final var statementResult = StatementResult.builder().more(null).build();

    // Then HasMore Is False
    assertThat(statementResult.hasMore(), is(false));
  }

  @Test
  void whenBuildingStatementResultWithMoreThenHasMoreIsTrue() {

    // When Building StatementResult With More
    final var statementResult = StatementResult.builder().more(URI.create("123")).build();

    // Then HasMore Is True
    assertThat(statementResult.hasMore(), is(true));
  }
}
