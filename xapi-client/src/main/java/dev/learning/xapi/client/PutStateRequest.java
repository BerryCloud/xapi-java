package dev.learning.xapi.client;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for putting a single State document.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
 *      State Document PUT</a>
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
public class PutStateRequest extends StateRequest {

  /**
   * The state object to store.
   */
  @NonNull
  private final Object state;

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.PUT;
  }

}
