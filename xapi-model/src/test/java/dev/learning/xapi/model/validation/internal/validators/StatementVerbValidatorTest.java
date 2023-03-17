/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * StatementVerbValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("StatementVerbValidator tests")
class StatementVerbValidatorTest {

  private static final StatementVerbValidator validator = new StatementVerbValidator();
  
  @Test
  void whenValueIsNullThenResultIsTrue() {
    
    // When Value Is Null
    var result = validator.isValid(null, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenValueHasNoVerbThenResultIsTrue() {
    
    // When Value has No Verb
    var value = Statement.builder().build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasNonVoidedVerbThenResultIsTrue() {
    
    // When Value has Non-Voided Verb 
    var value = Statement.builder().verb(Verb.ANSWERED).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueHasVoidedVerbButNoObjectThenResultIsFalse() {
    
    // When Value has Voided Verb But No Object
    var value = Statement.builder().verb(Verb.VOIDED).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasVoidedVerbAndInvalidObjectThenResultIsFalse() {
    
    // When Value has Voided Verb And Invalid Object
    var value = Statement.builder().verb(Verb.VOIDED)
        
        .activityObject(a->a.id(URI.create("http://example.com/activity")))
        
        .build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueHasVoidedVerbAndValidObjectThenResultIsTrue() {
    
    // When Value has Voided Verb And Valid Object
    var value = Statement.builder().verb(Verb.VOIDED)
        
        .statementReferenceObject(sr->sr.id(UUID.randomUUID()))
        
        .build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }
}
