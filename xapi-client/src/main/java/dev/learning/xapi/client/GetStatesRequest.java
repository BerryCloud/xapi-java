package dev.learning.xapi.client;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting multiple State documents.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-get">Multiple
 *      State Document GET</a>
 * @author István Rátkai (Selindek)
 * @param <T>
 */
@SuperBuilder
@Getter
public class GetStatesRequest<T> extends StatesRequest {

  private final Instant since;

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  @Override
  protected UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return super.url(uriBuilder, queryParams).queryParamIfPresent("since",
        Optional.ofNullable(since));

  }

}
