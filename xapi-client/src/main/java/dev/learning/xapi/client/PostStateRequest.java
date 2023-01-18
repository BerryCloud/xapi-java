package dev.learning.xapi.client;

import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpHeaders;
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
@SuperBuilder()
@Getter
@EqualsAndHashCode(callSuper = true)
public class PostStateRequest extends DeleteStateRequest {

  /**
   * The <strong>Content-Type</strong> header of the request. Default is
   * <code>application/json</code>.
   */
  @NonNull
  @Default
  private MediaType contentType = MediaType.APPLICATION_JSON;

  /**
   * The body of the request.
   */
  @NonNull
  private final Object body;

  @Override
  protected void headers(HttpHeaders headers) {
    super.headers(headers);
    headers.setContentType(contentType);
  }

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.POST;
  }

}
