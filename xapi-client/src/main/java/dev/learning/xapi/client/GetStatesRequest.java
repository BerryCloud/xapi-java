package dev.learning.xapi.client;

import java.util.List;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for getting multiple State documents.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#multiple-document-get">Multiple
 *      State Document GET</a>
 * @author István Rátkai (Selindek)
 */
@SuperBuilder
public class GetStatesRequest extends StatesRequest<List<String>> {

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.GET;
  }

}
