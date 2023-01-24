package dev.learning.xapi.client;

import java.util.Map;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.util.UriBuilder;

/**
 * Base class for xAPI request.
 *
 * @author István Rátkai (Selindek)
 * @param <T> The type of the response body. Can be {@link Void} for responses without body.
 */
@SuperBuilder
@Getter
@RequiredArgsConstructor
abstract class XapiRequest<T> {

  @NonNull
  @Default
  private final HttpHeaders httpHeaders = new HttpHeaders();

  @NonNull
  @SuppressWarnings("unchecked")
  public Class<T> getResponseType() {
    final var responseType =
        (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), XapiRequest.class);
    Assert.notNull(responseType, "XapiRequest resolved generic type must not be null");
    return responseType;
  }

  /**
   * Callback method which sets the query parameters for the xAPI request.
   *
   * @param uribuilder an {@link UriBuilder} object. The methods add query templates to the builder.
   * @param variableMap a {@link Map} containing the actual values for the query templates.
   */
  protected void query(UriBuilder uribuilder, Map<String, Object> variableMap) {}

  /**
   * Callback method which sets the headers for the xAPI request.
   *
   * @param headers a {@link HttpHeaders} object.
   */
  protected void headers(HttpHeaders httpHeaders) {
    httpHeaders.addAll(this.httpHeaders);

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
