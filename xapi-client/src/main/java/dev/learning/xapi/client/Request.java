package dev.learning.xapi.client;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.core.GenericTypeResolver;
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
abstract class Request<T> {

  @NonNull
  @SuppressWarnings("unchecked")
  public Class<T> getResponseType() {

    final var responseType =
        (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), Request.class);
    Assert.notNull(responseType, "XapiRequest resolved generic type must not be null");
    return responseType;
  }

  protected abstract UriBuilder url(UriBuilder uriBuilder);

  /**
   * The request method.
   *
   * @return the request method as a {@link HttpMethod} object.
   */
  protected abstract HttpMethod getMethod();

  /**
   * The request body. Default is <code>null</code>
   *
   * @return the request body.
   */
  protected Object getBody() {
    return null;
  }

}
