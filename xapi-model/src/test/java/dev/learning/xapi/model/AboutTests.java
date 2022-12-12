/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * About Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 * @author Thomas Turrell-Croft
 */
@DisplayName("About tests")
class AboutTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenDeserializingAboutThenResultIsInstanceOfAbout() throws IOException {

    final File file = ResourceUtils.getFile("classpath:about/about.json");

    // When Deserializing About
    final About result = objectMapper.readValue(file, About.class);

    // Then Result Is Instance Of About
    assertThat(result, instanceOf(About.class));

  }

  @Test
  void whenDeserializingAboutWithVersionThenVersionIsExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:about/about.json");

    // When Deserializing About With Version
    final About result = objectMapper.readValue(file, About.class);

    // Then Version Is Expected
    assertThat(result.getVersion(), is(new String[] {"1.0"}));

  }

  @Test
  void whenDeserializingAboutWithMultipleVersionsThenMultipleVersionsAreExpected()
      throws IOException {

    final File file = ResourceUtils.getFile("classpath:about/about_with_multiple_versions.json");

    // When Deserializing About With Multiple Versions
    final About result = objectMapper.readValue(file, About.class);

    // Then Multiple Versions Are Expected
    assertThat(result.getVersion(), is(new String[] {"1.0", "1.0.1"}));

  }


  @Test
  void whenDeserializingAboutWithExtensionsThenExtensionsAreExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:about/about_with_extensions.json");

    // When Deserializing About With Extensions
    final About result = objectMapper.readValue(file, About.class);

    // Then Extensions Are Expected
    assertThat(result.getExtensions().get(URI.create("http://url")), is("www.example.com"));

  }

  @Test
  void whenDeserializingAboutWithVersionAsNumberThenVersionIsExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:about/about_with_version_as_number.json");

    // When Deserializing About With Version As Number
    final About result = objectMapper.readValue(file, About.class);

    // Then Version Is Expected
    assertThat(result.getVersion(), is(new String[] {"1.0"}));
  }

  @Test
  void whenSerializingAboutThenResultIsEqualToExpectedJson() throws IOException {

    final About about = About.builder()

        .version(new String[] {"1.0"})

        .build();

    // When Serializing About
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(about));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:about/about.json"))));

  }

  @Test
  void whenSerializingAboutWithExtensionsThenResultIsEqualToExpectedJson() throws IOException {

    final LinkedHashMap<URI, Object> extensions = new LinkedHashMap<>();
    extensions.put(URI.create("http://url"), "www.example.com");

    final About about = About.builder()

        .version(new String[] {"1.0"})

        .extensions(extensions)

        .build();

    // When Serializing About With Extensions
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(about));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(objectMapper
        .readTree(ResourceUtils.getFile("classpath:about/about_with_extensions.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final LinkedHashMap<URI, Object> extensions = new LinkedHashMap<>();
    extensions.put(URI.create("http://url"), "www.example.com");

    final About about = About.builder()

        .version(new String[] {"1.0"})

        .extensions(extensions)

        .build();

    // When Calling ToString
    final String result = about.toString();

    // Then Result Is Expected
    assertThat(result, is("About(version=[1.0], extensions={http://url=www.example.com})"));

  }

}
