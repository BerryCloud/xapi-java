/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.disableable;

/**
 * Callback interface for disabling/enabling a {@link DisableableValidator}.
 *
 * @author István Rátkai (Selindek)
 */
@FunctionalInterface
public interface ValidatorDisabler {

  public static final ValidatorDisabler DEFAULT_DISABLER = v -> true;
  public static final ValidatorDisabler DEFAULT_ENABLER = v -> false;

  /**
   * Checks whether the given validator is disabled.
   *
   * @param validator the validator to check
   * @return true if the validator is disabled, false otherwise
   */
  public boolean isDisabled(DisableableValidator<?, ?> validator);
}
