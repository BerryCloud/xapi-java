package dev.learning.xapi.client;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import dev.learning.xapi.model.Agent;


@SpringBootTest(classes = TestApp.class)
public class StateRequestIT {

  @Autowired
  private XapiClient client;
 
  @Test
  public void t1 () {
    
    var activityId= URI.create("http://example.com");
    //var agent ="{\"name\":\"q&qq\",\"mbox\":\"mailto:admin@launchlearning.io\"}";
    var agent = Agent.builder().name("qqq&www").mbox("mailto:admin@launchlearning.io").build();
    var stateId = UUID.randomUUID().toString();
    
    var getRequest = GetStateRequest.builder()
        .activityId(activityId).agent(agent).stateId(stateId).build();
    var response = client.send(getRequest);
    
    assertThat(response.getStatusCode().value(), is(404));
  }
  
  @Test
  public void t2 () {
    
    var activityId= URI.create("http://example.com");
//    var agent ="{\"name\":\"qqq\",\"mbox\":\"mailto:admin@launchlearning.io\"}";
    var agent = Agent.builder().name("qqq&www").mbox("mailto:admin@launchlearning.io").build();
    var stateId = UUID.randomUUID().toString();

    var body = "qqq";
    var contentType = MediaType.TEXT_PLAIN_VALUE;
    
    var putRequest = PutStateRequest.builder()
        .activityId(activityId).agent(agent).stateId(stateId).body(body).contentType(contentType).build();
    var putResponse = client.send(putRequest);
    assertThat(putResponse.getStatusCode().value(), is(204));

    var getRequest = GetStateRequest.builder()
        .activityId(activityId).agent(agent).stateId(stateId).build();
    var response = client.send(getRequest);
    
    assertThat(response.getBody(), is(body));
  }
  
  @Test
  public void t3 () {
    
    var activityId= URI.create("http://example.com/"+UUID.randomUUID());
//    var agent ="{\"name\":\"qqq\",\"mbox\":\"mailto:admin@launchlearning.io\"}";
    var agent = Agent.builder().name("qqq&www").mbox("mailto:admin@launchlearning.io").build();
    
    var stateId1 = UUID.randomUUID().toString();
    var stateId2 = UUID.randomUUID().toString();

    var body = "qqq";
    var contentType = MediaType.TEXT_PLAIN_VALUE;
    
    var putRequest1 = PutStateRequest.builder()
        .activityId(activityId).agent(agent).stateId(stateId1).body(body).contentType(contentType).build();
    var putResponse1 = client.send(putRequest1);
    assertThat(putResponse1.getStatusCode().value(), is(204));

    var putRequest2 = PutStateRequest.builder()
        .activityId(activityId).agent(agent).stateId(stateId2).body(body).contentType(contentType).build();
    var putResponse2 = client.send(putRequest2);
    assertThat(putResponse2.getStatusCode().value(), is(204));

    var getRequest = GetStatesRequest.builder()
        .activityId(activityId).agent(agent).build();
    var response = client.send(getRequest);
    
    assertThat(response.getBody(), allOf(hasItem(stateId1),hasItem(stateId2)));
  }

}


