/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import jakarta.validation.Payload;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dev.learning.xapi.model.validation.constraints.Mbox;

/**
 * MboxValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("MboxValidator tests")
public class MboxValidatorTests {

  private static final MboxValidator validator = new MboxValidator();
  
  @BeforeAll
  static void init() {
    validator.initialize(new Mbox() {
      
      @Override
      public Class<? extends Annotation> annotationType() {
        return null;
      }
      
      @Override
      public Class<? extends Payload>[] payload() {
        return null;
      }
      
      @Override
      public String message() {
        return null;
      }
      
      @Override
      public Class<?>[] groups() {
        return null;
      }
    });
  }
  
  @Test
  void whenValueIsNullThenResultIsTrue() {
    
    // When Value Is Null
    var result = validator.isValid(null, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenValueIsValidMboxThenResultIsTrue() {
    
    // When Value Is Valid Mbox
    var result = validator.isValid("mailto:fred@example.com", null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenValueHasInvalidPrefixThenResultIsFalse() {
    
    // When Value Has Invalid Prefix
    var result = validator.isValid("email:fred@example.com", null);
    
    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasNoPrefixThenResultIsFalse() {
    
    // When Value Has No Prefix
    var result = validator.isValid("fred@example.com", null);
    
    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasInvalidEmailThenResultIsFalse() {
    
    // When Value Has Invalid Email
    var result = validator.isValid("mailto:fred@example@com", null);
    
    // Then Result Is False
    assertFalse(result);
  }

}
