package dev.learning.xapi.client;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import dev.learning.xapi.model.Actor;
import dev.learning.xapi.model.Agent;
import java.awt.Point;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(classes = TestApp.class)
public class StateRequestIT {

  @Autowired
  private XapiClient client;

  private URI activityId;
  private Actor agent;
  private String stateId;
  private Object body;
  private MediaType contentType;

  @BeforeEach
  public void init() {
    // set some default parameters
    activityId = URI.create("http://learning.dev/" + UUID.randomUUID());
    agent = Agent.builder().name("admin").mbox("mailto:admin@learning.dev").build();
    stateId = UUID.randomUUID().toString();
    body = new Point(1, 2);
    contentType = MediaType.APPLICATION_JSON;
  }

  @Test
  public void testGivenStateDoesNotExistWhenSendingGetStateRequestThenResponseStatusIsNotFound() {

    // Given State Does Not Exist

    // When Sending GetStateRequest

    final var getRequest = GetStateRequest.builder().activityId(activityId).agent(agent)
        .stateId(stateId).build();
    final var response = client.send(getRequest);

    // Then Response Status Is Not Found

    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void testGivenStateExistsWhenSendingGetStateRequestThenResponseBodyIsExpected() {

    // Given State Exists
    body = "text body";
    contentType = MediaType.TEXT_PLAIN;

    final var putRequest = PutStateRequest.builder().activityId(activityId).agent(agent)
        .stateId(stateId).body(body).contentType(contentType).build();
    final var putResponse = client.send(putRequest);
    assertThat(putResponse.getStatusCode().value(), is(204));

    // When Sending GetStateRequest

    final var getRequest = GetStateRequest.builder().activityId(activityId).agent(agent)
        .stateId(stateId).build();
    final var response = client.send(getRequest);

    // Then Response Body Is Expected

    assertThat(response.getBody(), is(body));
  }

  @Test
  public void testGivenStateExistsWhenSendingTypedGetStateRequestThenResponseBodyIsExpected() {

    // Given State Exists

    final var putRequest = PutStateRequest.builder().activityId(activityId).agent(agent)
        .stateId(stateId).body(body).contentType(contentType).build();
    final var putResponse = client.send(putRequest);
    assertThat(putResponse.getStatusCode().value(), is(204));

    // When Sending Typed GetStateRequest

    final var getRequest = GetStateRequest.builder().activityId(activityId).agent(agent)
        .stateId(stateId).build();
    final var response = client.send(getRequest, Point.class);

    // Then Response Body Is Expected

    assertThat(response.getBody(), is(body));
  }

  @Test
  public void testGivenMultipleStatesExistsWhenSendingGetStatesRequestThenResponseBodyIsExpected() {

    // Given Multiple States Exists

    final var stateId1 = UUID.randomUUID().toString();
    final var stateId2 = UUID.randomUUID().toString();

    final var putRequest1 = PutStateRequest.builder().activityId(activityId).agent(agent)
        .stateId(stateId1).body(body).contentType(contentType).build();
    final var putResponse1 = client.send(putRequest1);
    assertThat(putResponse1.getStatusCode().value(), is(204));

    final var putRequest2 = PutStateRequest.builder().activityId(activityId).agent(agent)
        .stateId(stateId2).body(body).contentType(contentType).build();
    final var putResponse2 = client.send(putRequest2);
    assertThat(putResponse2.getStatusCode().value(), is(204));

    // When Sending GetStatesRequest

    final var getRequest = GetStatesRequest.builder().activityId(activityId).agent(agent).build();
    final var response = client.send(getRequest);

    // Then Response Body Is Expected

    assertThat(response.getBody(), allOf(hasItem(stateId1), hasItem(stateId2)));
  }

}
