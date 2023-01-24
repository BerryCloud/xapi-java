package dev.learning.xapi.client;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class PostStateRequest extends StateRequest<Void> {

  /**
   * The <strong>Content-Type</strong> header of the request. Default is
   * <code>application/json</code>.
   */
  @NonNull
  private final MediaType contentType;

  /**
   * The state object to store.
   */
  @NonNull
  private final Object state;


  /*
   * @Override protected void headers(HttpHeaders headers) { super.headers(headers);
   * 
   * if (contentType != null) { headers.setContentType(contentType); } else if
   * (headers.getContentType() == null) { headers.setContentType(MediaType.APPLICATION_JSON); }
   * 
   * }
   */

  @Override
  protected HttpMethod getMethod() {
    return HttpMethod.POST;
  }

  @Override
  protected Object getBody() {

    return state;
  }



}
