/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class StatementFormatTests {

  @Test
  void whenCallingToStringOnCanonicalEnumResultIsLowerCase() {

    // When Calling ToString On Canonical Enum
    String result = StatementFormat.CANONICAL.toString();

    // Result Is Lower Case
    assertThat(result, is("canonical"));
  }

  @Test
  void whenCallingGetFormatOnCanonicalEnumResultIsLowerCase() {

    // When Calling GetFormat On Canonical Enum
    String result = StatementFormat.CANONICAL.getFormat();

    // Result Is Lower Case
    assertThat(result, is("canonical"));
  }

  @Test
  void whenCallingToStringOnExactEnumResultIsLowerCase() {

    // When Calling ToString On Exact Enum
    String result = StatementFormat.EXACT.toString();

    // Result Is Lower Case
    assertThat(result, is("exact"));
  }

  @Test
  void whenCallingGetFormatOnExactEnumResultIsLowerCase() {

    // When Calling GetFormat On Exact Enum
    String result = StatementFormat.EXACT.getFormat();

    // Result Is Lower Case
    assertThat(result, is("exact"));
  }

  @Test
  void whenCallingToStringOnIdsEnumResultIsLowerCase() {

    // When Calling ToString On Ids Enum
    String result = StatementFormat.IDS.toString();

    // Result Is Lower Case
    assertThat(result, is("ids"));
  }

  @Test
  void whenCallingGetFormatOnIdsEnumResultIsLowerCase() {

    // When Calling GetFormat On Ids Enum
    String result = StatementFormat.IDS.getFormat();

    // Result Is Lower Case
    assertThat(result, is("ids"));
  }

}
