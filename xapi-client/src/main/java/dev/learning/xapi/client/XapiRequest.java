package dev.learning.xapi.client;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Base class for xAPI request.
 *
 * @author István Rátkai (Selindek)
 * @param <T> The type of the response body. Can be {@link Void} for responses without body.
 */
@SuperBuilder()
@RequiredArgsConstructor
abstract class XapiRequest<T> {

  /**
   * Callback method which sets the query parameters for the xAPI request.
   *
   * @param uribuilder  an {@link UriBuilder} object. The methods add query templates to the
   *                    builder.
   * @param variableMap a {@link Map} containing the actual values for the query templates.
   */
  protected void query(UriBuilder uribuilder, Map<String, Object> variableMap) {
  }

  /**
   * Callback method which sets the headers for the xAPI request.
   *
   * @param headers a {@link HttpHeaders} object.
   */
  protected void headers(HttpHeaders headers) {
  }

  /**
   * The request method.
   *
   * @return the request method as a {@link HttpMethod} object.
   */
  protected abstract HttpMethod getMethod();

  /**
   * The path of the request endpoint.
   *
   * @return the path of the request endpoint as a String.
   */
  protected abstract String getPath();

  /**
   * The request body. Default is <code>null</code>
   *
   * @return the request body.
   */
  protected Object getBody() {
    return null;
  }

}
