/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Group;
import dev.learning.xapi.model.StatementReference;
import dev.learning.xapi.model.SubStatement;

/**
 * ActorValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("ActorValidator tests")
public class ActorValidatorTest {

  private static final ActorValidator validator = new ActorValidator();
  
  @Test
  void whenValueIsNullThenResultIsTrue() {
    
    // When Value Is Null
    var result = validator.isValid(null, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenValueIsActivityThenResultIsTrue() {
    
    // When Value Is Activity
    var value = Activity.builder().build();

    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenValueIsSubStatementThenResultIsTrue() {
    
    // When Value Is SubStatement
    var value = SubStatement.builder().build();

    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenValueIsStatementReferenceThenResultIsTrue() {
    
    // When Value Is StatementReference
    var value = StatementReference.builder().build();

    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenAgentValueHasNoIdentifierThenResultIsFalse() {
    
    // When Agent Value Has No Identifier
    var value = Agent.builder().build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }
  
  @Test
  void whenAgentValueHasOnlyMboxThenResultIsTrue() {
    
    // When Agent Value Has Only Mbox
    var value = Agent.builder().mbox("mailto:fred@example.com").build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenAgentValueHasOnlyMboxSha1sumThenResultIsTrue() {
    
    // When Agent Value Has Only MboxSha1sum
    var value = Agent.builder().mboxSha1sum("121212121212121212121212").build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenAgentValueHasOnlyOpenIdThenResultIsTrue() {
    
    // When Agent Value Has Only MboxSha1sum
    var value = Agent.builder().openid(URI.create("http://example.com/openid/121212121212121212")).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenAgentValueHasOnlyAccountThenResultIsTrue() {
    
    // When Agent Value Has Only Account
    var value = Agent.builder().account(a->a.homePage(URI.create("http://example.com")).name("fred")).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenAgentValueHasMoreThanOneIdentifierThenResultIsFalse() {
    
    // When Agent Value Has More Than One Identifier
    var value = Agent.builder().mbox("mailto:fred@example.com").mboxSha1sum("121212121212121212121212").build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }
  
  @Test
  void whenGroupValueHasNoIdentifierThenResultIsFalse() {
    
    // When Group Value Has No Identifier
    var value = Group.builder().build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }
  
  @Test
  void whenGroupValueHasNoIdentifierAndEmptyMembersThenResultIsFalse() {
    
    // When Group Value Has No Identifier And Empty Members
    var value = Group.builder().member(new ArrayList<>()).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }
  
  @Test
  void whenGroupValueHasNoIdentifierButHasMemberThenResultIsTrue() {
    
    // When Group Value Has No Identifier But Has Member
    var value = Group.builder().addMember(a->a.mbox("mailto:fred@example.com")).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }
  
  @Test
  void whenGroupValueHasOnlyMboxThenResultIsTrue() {
    
    // When Group Value Has Only Mbox
    var value = Group.builder().mbox("mailto:fred@example.com").build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenGroupValueHasOnlyMboxSha1sumThenResultIsTrue() {
    
    // When Group Value Has Only MboxSha1sum
    var value = Group.builder().mboxSha1sum("121212121212121212121212").build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenGroupValueHasOnlyOpenIdThenResultIsTrue() {
    
    // When Group Value Has Only MboxSha1sum
    var value = Group.builder().openid(URI.create("http://example.com/openid/121212121212121212")).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenGroupValueHasOnlyAccountThenResultIsTrue() {
    
    // When Group Value Has Only Account
    var value = Group.builder().account(a->a.homePage(URI.create("http://example.com")).name("fred")).build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenGroupValueHasMoreThanOneIdentifierThenResultIsFalse() {
    
    // When Group Value Has More Than One Identifier
    var value = Group.builder().mbox("mailto:fred@example.com").mboxSha1sum("121212121212121212121212").build();
    
    var result = validator.isValid(value, null);
    
    // Then Result Is False
    assertFalse(result);
  }

}
