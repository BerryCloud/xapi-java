package dev.learning.xapi.client;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Fetches Profile ids of all Profile documents for an Activity. If "since" parameter is specified,
 * this is limited to entries that have been stored or updated since the specified Timestamp
 * (exclusive).
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-get-2">Multiple
 *      Document GET</a>
 *
 * @author Thomas Turrell-Croft
 */
@Builder
public class GetActivityProfilesRequest implements Request {

  /**
   * The Activity id associated with these Profile documents.
   */
  @NonNull
  private URI activityId;

  /**
   * Only ids of Profile documents stored since the specified Timestamp (exclusive) are returned.
   */
  private Instant since;

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    queryParams.put("activityId", activityId);
    uriBuilder.path("/activities/profile").queryParam("activityId", "{activityId}");

    if (since != null) {
      queryParams.put("since", since);
      uriBuilder.queryParam("since", "{since}");
    }

    return uriBuilder;
  }

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  /**
   * Builder for GetActivityProfilesRequest.
   */
  public static class Builder {

    /**
     * Sets the activityId.
     *
     * @param activityId The activityId of the GetActivityProfilesRequest.
     *
     * @return This builder
     *
     * @see GetActivityProfilesRequest#activityId
     */
    public Builder activityId(URI activityId) {
      this.activityId = activityId;
      return this;
    }

    /**
     * Sets the activityId.
     *
     * @param activityId The activity of the GetActivityProfilesRequest.
     *
     * @return This builder
     *
     * @see GetActivityProfilesRequest#activityId
     */
    public Builder activityId(String activityId) {
      this.activityId = URI.create(activityId);
      return this;
    }

    // This static class extends the lombok builder.

  }
}
