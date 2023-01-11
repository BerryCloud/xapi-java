/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.resources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;

import java.io.File;
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

    final File file = ResourceUtils.getFile("classpath:statement_result/statement_result.json");

    // When Deserializing StatementResult
    final StatementResult result = objectMapper.readValue(file, StatementResult.class);

    // Then Result Is Instance Of StatementResult
    assertThat(result, instanceOf(StatementResult.class));

  }

  @Test
  void whenDeserializingStatementResultThenStatementsIsInstanceOfStatement() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement_result/statement_result.json");

    // When Deserializing StatementResult
    final StatementResult result = objectMapper.readValue(file, StatementResult.class);

    // Then Statements Is Instance Of Statement
    assertThat(result.getStatements()[0], instanceOf(Statement.class));

  }

  @Test
  void whenDeserializingStatementResultThenMoreIsExpected() throws Exception {

    final File file = ResourceUtils.getFile("classpath:statement_result/statement_result.json");

    // When Deserializing StatementResult
    final StatementResult result = objectMapper.readValue(file, StatementResult.class);

    // Then More Is Expected
    assertThat(result.getMore(), is(URI.create("123")));

  }

  @Test
  void whenSerializingStatementResultThenResultIsEqualToExpectedJson() throws IOException {

    final StatementResult statementResult = StatementResult.builder()

        .singleStatement(s -> s.id(UUID.fromString("fd41c918-b88b-4b20-a0a5-a4c32391aaa0")))

        .more(URI.create("123"))

        .build();

    // When Serializing StatementResult
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(statementResult));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(objectMapper.readTree(
        "{\"statements\":[{\"id\":\"fd41c918-b88b-4b20-a0a5-a4c32391aaa0\"}],\"more\":\"123\"}")));
  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final StatementResult statementResult = StatementResult.builder()

        .singleStatement(s -> s

            .id(UUID.fromString("fd41c918-b88b-4b20-a0a5-a4c32391aaa0"))

            .version("1.0.3"))

        .more(URI.create("123"))

        .build();

    // When Calling To String
    final String result = statementResult.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "StatementResult(statements=[Statement(id=fd41c918-b88b-4b20-a0a5-a4c32391aaa0, actor=null, verb=null, object=null, result=null, context=null, timestamp=null, stored=null, authority=null, version=1.0.3, attachments=null)], more=123)"));

  }


  @Test
  void whenBuildingStatementResultWithTwoStatementsThenStatmentsIsArrayWithSizeTwo() {

    // When Building StatementResult With Two Statements
    final StatementResult statementResult = StatementResult.builder()

        .singleStatement(s -> s.id(UUID.fromString("fd41c918-b88b-4b20-a0a5-a4c32391aaa0")))

        .singleStatement(s -> s.id(UUID.fromString("0b05db78-5554-4cde-8673-56ca15580d1b")))

        .more(URI.create("123"))

        .build();

    // Then Statments Is Array With Size Two
    assertThat(statementResult.getStatements(), arrayWithSize(2));

  }


}
