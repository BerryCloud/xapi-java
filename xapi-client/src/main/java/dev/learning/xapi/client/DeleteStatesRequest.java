package dev.learning.xapi.client;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for deleting multiple State documents.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-delete">Multiple
 *      State Document DELETE</a>
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DeleteStatesRequest extends StatesRequest<Void> {

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.DELETE;
  }

}
