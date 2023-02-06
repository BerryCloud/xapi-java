package dev.learning.xapi.client;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * Request for posting a single State document.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
 *      State Document POST</a>
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#json-procedure-with-requirements">JSON
 *      Procedure with Requirements</a>
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
@Getter
public class PostStateRequest extends StateRequest {

  @Default
  private MediaType contentType = MediaType.APPLICATION_JSON;

  /**
   * The state object to store.
   */
  @NonNull
  private final Object state;

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.POST;
  }

}
