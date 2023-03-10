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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

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

    final File file = ResourceUtils.getFile("classpath:group/group.json");

    // When Deserializing Group
    final Group result = objectMapper.readValue(file, Group.class);

    // Then Result Is Instance Of Group
    assertThat(result, instanceOf(Group.class));

  }

  @Test
  void whenDeserializingGroupWithMembersWithObjectTypeThenResultIsInstanceOfGroup()
      throws IOException {

    final File file =
        ResourceUtils.getFile("classpath:group/group_with_members_with_object_type.json");

    // When Deserializing Group With Members With Object Type
    final Group result = objectMapper.readValue(file, Group.class);

    // Then Result Is Instance Of Group
    assertThat(result, instanceOf(Group.class));

  }

  @Test
  void whenDeserializingGroupThenMemberIsInstanceOfAgent() throws IOException {

    final File file = ResourceUtils.getFile("classpath:group/group.json");

    // When Deserializing Group
    final Group result = objectMapper.readValue(file, Group.class);

    // Then Member Is Instance Of Agent
    assertThat(result.getMember().get(0), instanceOf(Agent.class));
  }

  @Test
  void whenSerializingGroupThenResultIsEqualToExpectedJson() throws IOException {

    final Group group = Group.builder()

        .name("Example Group")

        .addMember(a -> a.name("Member 1").mbox("mailto:member1@example.com"))

        .addMember(a -> a.name("Member 2").openid(URI.create("https://example.com/openId")))

        .account(a -> a.homePage(URI.create("http://example.com/homePage")).name("GroupAccount"))

        .build();

    // When Serializing Group
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(group));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:group/group.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final Group group =
        objectMapper.readValue(ResourceUtils.getFile("classpath:group/group.json"), Group.class);

    // When Calling ToString
    final String result = group.toString();

    // Then Result Is Expected
    assertThat(result, is(
        "Group(super=Actor(name=Example Group, mbox=null, mboxSha1sum=null, openid=null, account=Account(homePage=http://example.com/homePage, name=GroupAccount)), member=[Agent(super=Actor(name=Member 1, mbox=mailto:member1@example.com, mboxSha1sum=null, openid=null, account=null)), Agent(super=Actor(name=Member 2, mbox=null, mboxSha1sum=null, openid=https://example.com/openId, account=null))])"));

  }

}
