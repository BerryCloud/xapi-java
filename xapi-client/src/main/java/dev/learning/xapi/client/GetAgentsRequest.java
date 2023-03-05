package dev.learning.xapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import java.util.Map;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Return a special, Person Object for a specified Agent. The Person Object is very similar to an
 * Agent Object, but instead of each attribute having a single value, each attribute has an array
 * value, and it is legal to include multiple identifying properties.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#24-agents-resourcehttps://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#24-agents-resource">Agents
 *      Resource</a>
 *
 * @author Thomas Turrell-Croft
 */
@Builder
public class GetAgentsRequest implements Request {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * The Agent representation to use in fetching expanded Agent information.
   */
  @NonNull
  private Agent agent;

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("agent", agentToJsonString());

    return uriBuilder.path("/agents").queryParam("agent", "{agent}");
  }

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  private String agentToJsonString() {

    try {
      return objectMapper.writeValueAsString(agent);
    } catch (JsonProcessingException e) {
      // Should not happen
      return null;
    }

  }

}
