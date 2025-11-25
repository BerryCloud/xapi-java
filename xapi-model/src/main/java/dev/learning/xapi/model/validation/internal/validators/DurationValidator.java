/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.ValidDuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates ISO 8601:2004 duration format strings.
 *
 * <p>Supports formats: P[n]W, P[n]Y[n]M[n]DT[n]H[n]M[n]S and variations.
 *
 * @author Berry Cloud
 */
public class DurationValidator implements ConstraintValidator<ValidDuration, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    String upper = value.toUpperCase();

    // Must start with P
    if (!upper.startsWith("P") || upper.length() < 2) {
      return false;
    }

    String rest = upper.substring(1);

    // Week format: P[n]W
    if (rest.endsWith("W") && rest.length() > 1) {
      return isDigits(rest.substring(0, rest.length() - 1));
    }

    // Split by T to get date and time parts
    int tpos = rest.indexOf('T');
    String datePart = tpos >= 0 ? rest.substring(0, tpos) : rest;
    String timePart = tpos >= 0 ? rest.substring(tpos + 1) : "";

    // Must have at least one component
    if (datePart.isEmpty() && timePart.isEmpty()) {
      return false;
    }

    // Validate date part (Y, M, D in order)
    if (!datePart.isEmpty() && !isValidPart(datePart, "YMD", false)) {
      return false;
    }

    // Validate time part (H, M, S in order, S can be decimal)
    if (!timePart.isEmpty() && !isValidPart(timePart, "HMS", true)) {
      return false;
    }

    return true;
  }

  private boolean isValidPart(String part, String designators, boolean allowDecimalLast) {
    int pos = 0;
    boolean found = false;

    for (int i = 0; i < designators.length(); i++) {
      char designator = designators.charAt(i);
      int idx = part.indexOf(designator, pos);
      if (idx > pos) {
        String digits = part.substring(pos, idx);
        boolean isLast = i == designators.length() - 1;
        if (!(isLast && allowDecimalLast ? isDigitsOrDecimal(digits) : isDigits(digits))) {
          return false;
        }
        pos = idx + 1;
        found = true;
      } else if (idx == pos) {
        return false; // Designator without preceding digits
      }
    }

    return found && pos == part.length();
  }

  private boolean isDigits(String s) {
    if (s.isEmpty()) {
      return false;
    }
    for (int i = 0; i < s.length(); i++) {
      if (!Character.isDigit(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private boolean isDigitsOrDecimal(String s) {
    if (s.isEmpty()) {
      return false;
    }
    int dotPos = s.indexOf('.');
    if (dotPos < 0) {
      return isDigits(s);
    }
    return dotPos > 0
        && dotPos < s.length() - 1
        && isDigits(s.substring(0, dotPos))
        && isDigits(s.substring(dotPos + 1));
  }
}
