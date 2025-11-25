/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.validation.constraints.ValidDuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Validates ISO 8601:2004 duration format strings.
 *
 * <p>This validator uses a programmatic approach to avoid complex regex patterns that could be
 * flagged as code smells. The validation is broken down into logical parts:
 *
 * <ul>
 *   <li>Week format: P[n]W (e.g., P1W, P52W)
 *   <li>Date/time format: P[n]Y[n]M[n]DT[n]H[n]M[n]S or P[n]Y[n]M[n]D
 * </ul>
 *
 * <p>Uses possessive quantifiers (++) to prevent ReDoS attacks.
 *
 * @author Berry Cloud
 */
public class DurationValidator implements ConstraintValidator<ValidDuration, String> {

  // Simple patterns using possessive quantifiers to prevent ReDoS
  private static final Pattern WEEK_PATTERN =
      Pattern.compile("^P\\d++W$", Pattern.CASE_INSENSITIVE);
  private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d++$");
  private static final Pattern DECIMAL_PATTERN = Pattern.compile("^\\d++\\.\\d++$");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    // Check for week format first (P[n]W)
    if (WEEK_PATTERN.matcher(value).matches()) {
      return true;
    }

    // Must start with P
    if (!value.toUpperCase().startsWith("P")) {
      return false;
    }

    // Remove P prefix for processing
    String remaining = value.substring(1);

    // Empty after P is invalid
    if (remaining.isEmpty()) {
      return false;
    }

    // Check if there's a time component (T separator)
    int timeIndex = remaining.toUpperCase().indexOf('T');

    String datePart;
    String timePart;

    if (timeIndex >= 0) {
      datePart = remaining.substring(0, timeIndex);
      timePart = remaining.substring(timeIndex + 1);

      // T must be followed by time components
      if (timePart.isEmpty()) {
        return false;
      }
    } else {
      datePart = remaining;
      timePart = null;
    }

    // Validate date part (Y, M, D components)
    if (!datePart.isEmpty() && !isValidDatePart(datePart.toUpperCase())) {
      return false;
    }

    // Validate time part (H, M, S components)
    if (timePart != null && !isValidTimePart(timePart.toUpperCase())) {
      return false;
    }

    // Must have at least one component
    return !datePart.isEmpty() || timePart != null;
  }

  /**
   * Validates the date part of the duration (Y, M, D components).
   *
   * <p>Components must appear in order: Years, Months, Days.
   */
  private boolean isValidDatePart(String datePart) {
    if (datePart.isEmpty()) {
      return true;
    }

    int pos = 0;

    // Check for Years
    int yearIndex = datePart.indexOf('Y');
    if (yearIndex >= 0) {
      if (yearIndex == 0) {
        return false; // No digits before Y
      }
      String digits = datePart.substring(pos, yearIndex);
      if (!isValidDigits(digits)) {
        return false;
      }
      pos = yearIndex + 1;
    }

    // Check for Months
    int monthIndex = datePart.indexOf('M', pos);
    if (monthIndex >= 0) {
      if (monthIndex == pos) {
        return false; // No digits before M
      }
      String digits = datePart.substring(pos, monthIndex);
      if (!isValidDigits(digits)) {
        return false;
      }
      pos = monthIndex + 1;
    }

    // Check for Days
    int dayIndex = datePart.indexOf('D', pos);
    if (dayIndex >= 0) {
      if (dayIndex == pos) {
        return false; // No digits before D
      }
      String digits = datePart.substring(pos, dayIndex);
      if (!isValidDigits(digits)) {
        return false;
      }
      pos = dayIndex + 1;
    }

    // Nothing should remain after processing
    return pos == datePart.length();
  }

  /**
   * Validates the time part of the duration (H, M, S components).
   *
   * <p>Components must appear in order: Hours, Minutes, Seconds. Only seconds can have decimals.
   */
  private boolean isValidTimePart(String timePart) {
    if (timePart.isEmpty()) {
      return false; // T must be followed by components
    }

    int pos = 0;

    // Check for Hours
    int hourIndex = timePart.indexOf('H');
    if (hourIndex >= 0) {
      if (hourIndex == 0) {
        return false; // No digits before H
      }
      String digits = timePart.substring(pos, hourIndex);
      if (!isValidDigits(digits)) {
        return false;
      }
      pos = hourIndex + 1;
    }

    // Check for Minutes
    int minuteIndex = timePart.indexOf('M', pos);
    if (minuteIndex >= 0) {
      if (minuteIndex == pos) {
        return false; // No digits before M
      }
      String digits = timePart.substring(pos, minuteIndex);
      if (!isValidDigits(digits)) {
        return false;
      }
      pos = minuteIndex + 1;
    }

    // Check for Seconds (can be decimal)
    int secondIndex = timePart.indexOf('S', pos);
    if (secondIndex >= 0) {
      if (secondIndex == pos) {
        return false; // No digits before S
      }
      String digits = timePart.substring(pos, secondIndex);
      if (!isValidDigitsOrDecimal(digits)) {
        return false;
      }
      pos = secondIndex + 1;
    }

    // Nothing should remain after processing and at least one component must be present
    return pos == timePart.length() && timePart.length() > 0;
  }

  /** Checks if the string contains only digits. */
  private boolean isValidDigits(String value) {
    return !value.isEmpty() && DIGITS_PATTERN.matcher(value).matches();
  }

  /** Checks if the string contains digits or a decimal number. */
  private boolean isValidDigitsOrDecimal(String value) {
    if (value.isEmpty()) {
      return false;
    }
    return DIGITS_PATTERN.matcher(value).matches() || DECIMAL_PATTERN.matcher(value).matches();
  }
}
