package dev.learning.xapi.client;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Abstract superclass for state requests manipulating a single state document.
 *
 * @author István Rátkai (Selindek)
 * @param <T> The response type of the request
 */
@SuperBuilder
@Getter
abstract class StateRequest<T> extends StatesRequest<T> {

  /**
   * The <strong>stateId</strong> query parameter.
   */
  @NonNull
  private final String stateId;


}
