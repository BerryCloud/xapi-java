/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.ValidDuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates ISO 8601:2004 duration format strings.
 *
 * <p>Supports formats: P[n]W, P[n]Y[n]M[n]DT[n]H[n]M[n]S and variations.
 *
 * @author Berry Cloud
 */
public class DurationValidator implements ConstraintValidator<ValidDuration, String> {

  // Simple patterns - each validates a single component type
  private static final Pattern WEEK = Pattern.compile("^\\d+W$", Pattern.CASE_INSENSITIVE);
  private static final Pattern DATE =
      Pattern.compile("^(\\d+Y)?(\\d+M)?(\\d+D)?$", Pattern.CASE_INSENSITIVE);
  private static final Pattern TIME =
      Pattern.compile("^(\\d+H)?(\\d+M)?((\\d+\\.\\d+|\\d+)S)?$", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    if (!value.toUpperCase().startsWith("P") || value.length() < 2) {
      return false;
    }

    String rest = value.substring(1);

    if (WEEK.matcher(rest).matches()) {
      return true;
    }

    int tpos = rest.toUpperCase().indexOf('T');
    String datePart = tpos >= 0 ? rest.substring(0, tpos) : rest;
    String timePart = tpos >= 0 ? rest.substring(tpos + 1) : "";

    if (datePart.isEmpty() && timePart.isEmpty()) {
      return false;
    }

    return isValidDatePart(datePart) && isValidTimePart(timePart);
  }

  private boolean isValidDatePart(String datePart) {
    if (datePart.isEmpty()) {
      return true;
    }
    Matcher m = DATE.matcher(datePart);
    return m.matches() && (m.group(1) != null || m.group(2) != null || m.group(3) != null);
  }

  private boolean isValidTimePart(String timePart) {
    if (timePart.isEmpty()) {
      return true;
    }
    Matcher m = TIME.matcher(timePart);
    return m.matches() && (m.group(1) != null || m.group(2) != null || m.group(3) != null);
  }
}
