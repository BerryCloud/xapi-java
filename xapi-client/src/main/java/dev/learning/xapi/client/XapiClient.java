package dev.learning.xapi.client;

import dev.learning.xapi.model.ActivityState;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Client for communicating with LRS or service which implements some of the xAPI communication
 * resources.
 *
 * @author Thomas Turrell-Croft
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#20-resources">xAPI
 *      communication resources</a>
 */
public class XapiClient {

  private final WebClient client;

  public XapiClient(WebClient.Builder builder) {
    this.client = builder.baseUrl(" http://example.com/xAPI/").build();
  }

  /**
   * Gets a state document.
   */
  public ActivityState getState(StateParms params) {

    // TODO Auto-generated method stub

    final ActivityState activityState = client.get()
        .uri(uriBuilder -> uriBuilder.path("activities/state")

            .queryParam("activityId", params.getActivityId())

            .queryParam("agent", params.getAgent())

            .queryParam("stateId", params.getStateId())

            .queryParamIfPresent("deliveryDate", params.getRegistration())

            .build())

        .retrieve()

        .bodyToMono(ActivityState.class)

        .block();

    return activityState;

  }

}
