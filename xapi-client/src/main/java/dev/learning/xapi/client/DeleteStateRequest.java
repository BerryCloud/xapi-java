package dev.learning.xapi.client;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpHeaders;
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
@Getter
@EqualsAndHashCode(callSuper = true)
public class DeleteStateRequest extends StateRequest<Void> {

  /**
   * The <strong>If-Match</strong> header of the request.
   */
  private final String match;

  /**
   * The <strong>If-None-Match</strong> headers of the request.
   */
  private final List<String> noneMatch;

  @Override
  protected void headers(HttpHeaders headers) {
    if (match != null) {
      headers.set(HttpHeaders.IF_MATCH, match);
    }
    if (noneMatch != null && !noneMatch.isEmpty()) {
      headers.addAll(HttpHeaders.IF_NONE_MATCH, noneMatch);
    }
  }

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.DELETE;
  }

}
