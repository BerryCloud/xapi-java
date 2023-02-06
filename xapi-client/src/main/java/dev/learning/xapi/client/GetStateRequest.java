package dev.learning.xapi.client;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

/**
 * Request for getting a single State document.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#single-document-put--post--get--delete">Single
 *      State Document GET</a>
 * @author István Rátkai (Selindek)
 * @param <T>
 */
@SuperBuilder
@Getter
public class GetStateRequest extends StateRequest {

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.GET;
  }


}
