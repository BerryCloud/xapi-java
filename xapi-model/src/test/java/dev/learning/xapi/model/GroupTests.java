/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import tools.jackson.databind.ObjectMapper;

/**
 * Group Tests.
 *
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("Group tests")
class GroupTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenDeserializingGroupThenResultIsInstanceOfGroup() throws IOException {

    final var file = ResourceUtils.getFile("classpath:group/group.json");

    // When Deserializing Group
    final var result = objectMapper.readValue(file, Group.class);

    // Then Result Is Instance Of Group
    assertThat(result, instanceOf(Group.class));
  }

  @Test
  void whenDeserializingGroupWithMembersWithObjectTypeThenResultIsInstanceOfGroup()
      throws IOException {

    final var file =
        ResourceUtils.getFile("classpath:group/group_with_members_with_object_type.json");

    // When Deserializing Group With Members With Object Type
    final var result = objectMapper.readValue(file, Group.class);

    // Then Result Is Instance Of Group
    assertThat(result, instanceOf(Group.class));
  }

  @Test
  void whenDeserializingGroupThenMemberIsInstanceOfAgent() throws IOException {

    final var file = ResourceUtils.getFile("classpath:group/group.json");

    // When Deserializing Group
    final var result = objectMapper.readValue(file, Group.class);

    // Then Member Is Instance Of Agent
    assertThat(result.getMember().get(0), instanceOf(Agent.class));
  }

  @Test
  void whenSerializingGroupThenResultIsEqualToExpectedJson() throws IOException {

    final Group group =
        Group.builder()
            .name("Example Group")
            .addMember(a -> a.name("Member 1").mbox("mailto:member1@example.com"))
            .addMember(a -> a.name("Member 2").openid(URI.create("https://example.com/openId")))
            .account(
                a -> a.homePage(URI.create("http://example.com/homePage")).name("GroupAccount"))
            .build();

    // When Serializing Group
    final var result = objectMapper.readTree(objectMapper.writeValueAsString(group));

    // Then Result Is Equal To Expected Json
    assertThat(
        result, is(objectMapper.readTree(ResourceUtils.getFile("classpath:group/group.json"))));
  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final var group =
        objectMapper.readValue(ResourceUtils.getFile("classpath:group/group.json"), Group.class);

    // When Calling ToString
    final var result = group.toString();

    // Then Result Is Expected
    assertThat(
        result,
        is(
            "Group(super=Actor(name=Example Group, mbox=null, mboxSha1sum=null, openid=null, account=Account(homePage=http://example.com/homePage, name=GroupAccount)), objectType=Group, member=[Agent(super=Actor(name=Member 1, mbox=mailto:member1@example.com, mboxSha1sum=null, openid=null, account=null), objectType=null), Agent(super=Actor(name=Member 2, mbox=null, mboxSha1sum=null, openid=https://example.com/openId, account=null), objectType=null)])"));
  }

  @Test
  void givenGroupWithNameAndMembersWhenCallingIsAnonymousThenResultIsTrue() {

    // Given Group With Name And Members
    final Group group =
        Group.builder()
            .name("Example Group")
            .addMember(a -> a.name("Member 1").mbox("mailto:member1@example.com"))
            .addMember(a -> a.name("Member 2").openid(URI.create("https://example.com/openId")))
            .build();

    // When Calling Is Anonymous
    final var result = group.isAnonymous();

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void givenGroupWithNameAndNoMembersWhenCallingIsAnonymousThenResultIsFalse() {

    // Given Group With Name And Empty Members
    final Group group =
        Group.builder().name("Example Group").member(new ArrayList<Agent>()).build();

    // When Calling Is Anonymous
    final var result = group.isAnonymous();

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void givenGroupWithNameAndNullMembersWhenCallingIsAnonymousThenResultIsFalse() {

    // Given Group With Name And Null Members
    final Group group = Group.builder().name("Example Group").member(null).build();

    // When Calling Is Anonymous
    final var result = group.isAnonymous();

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void givenGroupWithMboxAndMembersWhenCallingIsAnonymousThenResultIsFalse() {

    // Given Group With MBox And Members
    final Group group =
        Group.builder()
            .mbox("mailto:another@example.com")
            .addMember(a -> a.name("Member 1").mbox("mailto:member1@example.com"))
            .addMember(a -> a.name("Member 2").openid(URI.create("https://example.com/openId")))
            .build();

    // When Calling Is Anonymous
    final var result = group.isAnonymous();

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void givenGroupWithMboxSha1sumAndMembersWhenCallingIsAnonymousThenResultIsFalse() {

    // Given Group With MboxSha1sum And Members
    final Group group =
        Group.builder()
            .mboxSha1sum("mailto:another@example.com")
            .addMember(a -> a.name("Member 1").mbox("mailto:member1@example.com"))
            .addMember(a -> a.name("Member 2").openid(URI.create("https://example.com/openId")))
            .build();

    // When Calling Is Anonymous
    final var result = group.isAnonymous();

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void givenGroupWithOpenIDAndMembersWhenCallingIsAnonymousThenResultIsFalse() {

    // Given Group With OpenID And Members
    final Group group =
        Group.builder()
            .openid(URI.create("https://example.com"))
            .addMember(a -> a.name("Member 1").mbox("mailto:member1@example.com"))
            .addMember(a -> a.name("Member 2").openid(URI.create("https://example.com/openId")))
            .build();

    // When Calling Is Anonymous
    final var result = group.isAnonymous();

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void givenGroupWithAccountAndMembersWhenCallingIsAnonymousThenResultIsFalse() {

    // Given Group With Account And Members
    final Group group =
        Group.builder()
            .addMember(a -> a.name("Member 1").mbox("mailto:member1@example.com"))
            .addMember(a -> a.name("Member 2").openid(URI.create("https://example.com/openId")))
            .account(a -> a.name("name").homePage(URI.create("https://example.com")))
            .build();

    // When Calling Is Anonymous
    final var result = group.isAnonymous();

    // Then Result Is False
    assertFalse(result);
  }
}
