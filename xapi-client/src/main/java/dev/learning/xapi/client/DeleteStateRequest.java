package dev.learning.xapi.client;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for deleting a single State document.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
 *      State Document DELETE</a>
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
public class DeleteStateRequest extends StateRequest {

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.DELETE;
  }

}
