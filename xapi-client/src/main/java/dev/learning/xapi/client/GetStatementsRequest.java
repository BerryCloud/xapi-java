/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.StatementFormat;
import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting multiple State documents.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-get">Multiple
 *      State Document GET</a>
 *
 * @author István Rátkai (Selindek)
 */
@Builder
@Getter
public class GetStatementsRequest implements Request {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final Agent agent;

  private final URI verb;

  private final URI activity;

  private final UUID registration;

  private final Boolean relatedActivities;

  private final Boolean relatedAgents;

  private final Instant since;

  private final Instant until;

  private final Integer limit;

  private final StatementFormat format;

  private final Boolean attachments;

  private final Boolean ascending;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    // All queryParams are optional

    uriBuilder.path("statements");

    if (agent != null) {
      queryParams.put("agent", agentToJsonString());
      uriBuilder.queryParam("verb", "{agent}");
    }

    if (verb != null) {
      queryParams.put("verb", verb);
      uriBuilder.queryParam("verb", "{verb}");
    }

    if (verb != null) {
      queryParams.put("activity", activity);
      uriBuilder.queryParam("activity", "{activity}");
    }

    if (since != null) {
      queryParams.put("since ", since);
      uriBuilder.queryParam("since ", "{since}");
    }

    if (since != null) {
      queryParams.put("until ", until);
      uriBuilder.queryParam("until ", "{until}");
    }

    return uriBuilder

        .queryParamIfPresent("agent", templateIfParamPresent("{agent}", agent))

        .queryParamIfPresent("verb", templateIfParamPresent("{verb}", verb))

        .queryParamIfPresent("activity", templateIfParamPresent("{activity}", activity))

        .queryParamIfPresent("registration", Optional.ofNullable(registration))

        .queryParamIfPresent("related_activities", Optional.ofNullable(relatedActivities))

        .queryParamIfPresent("related_agents", Optional.ofNullable(relatedAgents))

        .queryParamIfPresent("since", templateIfParamPresent("{since}", since))

        .queryParamIfPresent("until", templateIfParamPresent("{until}", until))

        .queryParamIfPresent("limit", Optional.ofNullable(limit))

        .queryParamIfPresent("format", Optional.ofNullable(format))

        .queryParamIfPresent("attachments", Optional.ofNullable(attachments))

        .queryParamIfPresent("ascending", Optional.ofNullable(ascending));

  }

  private Optional<String> templateIfParamPresent(String template, Object value) {

    if (value == null) {
      return Optional.empty();
    }

    return Optional.of(template);
  }

  /**
   * Builder for Verb.
   */
  public static class Builder {


    /**
     * Sets the agent.
     *
     * @param agent The agent of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementsRequest#agent
     */
    public Builder agent(Agent agent) {
      this.agent = agent;
      return this;
    }

    /**
     * Sets the agent.
     *
     * @param agent The agent of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementsRequest#agent
     */
    public Builder agent(Consumer<Agent.Builder<?, ?>> agent) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      agent.accept(builder);

      return agent(builder.build());
    }

    /**
     * Sets the verb.
     *
     * @param verb The verb of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementsRequest#verb
     */
    public Builder verb(URI verb) {
      this.verb = verb;
      return this;
    }

    /**
     * Sets the verb.
     *
     * @param verb The verb of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementsRequest#verb
     */
    public Builder verb(String verb) {
      this.verb = URI.create(verb);
      return this;
    }

    /**
     * Sets the activity.
     *
     * @param activity The activity of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementsRequest#activity
     */
    public Builder activity(URI activity) {
      this.activity = activity;
      return this;
    }

    /**
     * Sets the activity.
     *
     * @param activity The activity of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementsRequest#activity
     */
    public Builder activity(String activity) {
      this.activity = URI.create(activity);
      return this;
    }



    /**
     * Sets the registration.
     *
     * @param registration The registration of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementsRequest#registration
     */
    public Builder registration(UUID registration) {
      this.registration = registration;
      return this;
    }

    /**
     * Sets the registration.
     *
     * @param registration The registration of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementsRequest#registration
     */
    public Builder registration(String registration) {
      this.registration = UUID.fromString(registration);
      return this;
    }

    // This static class extends the lombok builder.

  }

  private String agentToJsonString() {
    if (agent == null) {
      return null;
    }

    try {
      return objectMapper.writeValueAsString(agent);
    } catch (JsonProcessingException e) {
      // Should not happen
      return null;
    }

  }

}
