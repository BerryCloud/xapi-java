/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Result Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("Result tests")
class ResultTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenDeserializingResultThenResultIsInstanceOfResult() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Result Is Instance Of Result
    assertThat(result, instanceOf(Result.class));
  }

  @Test
  void whenDeserializingResultThenScoreIsInstanceOfScore() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Score Is Instance Of Score
    assertThat(result.getScore(), instanceOf(Score.class));
  }

  @Test
  void whenDeserializingResultThenSuccessIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Success Is Expected
    assertThat(result.getSuccess(), is(true));
  }

  @Test
  void whenDeserializingResultThenCompletionIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Completion Is Expected
    assertThat(result.getCompletion(), is(true));
  }

  @Test
  void whenDeserializingResultThenResponseIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Response Is Expected
    assertThat(result.getResponse(), is("test"));
  }

  @Test
  void whenDeserializingResultThenDurationIsExpected() throws Exception {

    final var file = ResourceUtils.getFile("classpath:result/result.json");

    // When Deserializing Result
    final var result = objectMapper.readValue(file, Result.class);

    // Then Duration Is Expected
    assertThat(result.getDuration(), is("P1D"));
  }

  @Test
  void whenSerializingResultThenResultIsEqualToExpectedJson() throws IOException {

    final var resultInstance =
        Result.builder()
            .score(s -> s.max(5.0f).min(0.0f).raw(1.0f).scaled(1.0f))
            .completion(true)
            .success(true)
            .duration("P1D")
            .response("test")
            .build();

    // When Serializing Result
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(resultInstance));

    // Then Result Is Equal To Expected Json
    assertThat(
        result, is(objectMapper.readTree(ResourceUtils.getFile("classpath:result/result.json"))));
  }

  @Test
  void whenCallingToStringThenResultIsExpected() {

    final var resultInstance =
        Result.builder()
            .score(s -> s.max(5.0f).min(0.0f).raw(1.0f).scaled(1.0f))
            .completion(true)
            .success(true)
            .duration("P1D")
            .response("test")
            .build();

    // When Calling ToString
    final var result = resultInstance.toString();

    // Then Result Is Expected
    assertThat(
        result,
        is(
            "Result(score=Score(scaled=1.0, raw=1.0, min=0.0, max=5.0), success=true, completion=true, "
                + "response=test, duration=P1D, extensions=null)"));
  }
}
