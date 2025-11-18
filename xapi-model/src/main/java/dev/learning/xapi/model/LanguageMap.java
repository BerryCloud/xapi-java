/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.learning.xapi.jackson.LocaleSerializer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Map;

/**
 * A language map is a dictionary where the key is a RFC 5646 Language Tag, and the value is a
 * string in the language specified in the tag.
 *
 * @author Thomas Turrell-Croft
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#lang-maps">Language
 *      Maps</a>
 */
@JsonSerialize(keyUsing = LocaleSerializer.LocaleKeySerializer.class)
public class LanguageMap extends LinkedHashMap<Locale, String> {

  private static final long serialVersionUID = 7375610804995032187L;

  /**
   * Cached undefined locale instance to avoid repeated object creation.
   */
  private static final Locale UNDEFINED_LOCALE = Locale.forLanguageTag("und");

  /**
   * Constructs an empty LanguageMap.
   */
  public LanguageMap() {
    super();
  }

  /**
   * Constructs an new LanguageMap with the same mappings as the specified Map.
   *
   * @param languageMap the map whose mappings are to be placed in this LanguageMap
   */
  public LanguageMap(Map<Locale, String> languageMap) {
    super(languageMap);
  }

  /**
   * Puts a value in the language map with the undefined locale as a key. If the language map
   * previously contained a mapping for the undefined locale, the old value is replaced.
   *
   * @param value to be added with the undefined locale as a key
   */
  public void put(String value) {
    this.put(UNDEFINED_LOCALE, value);
  }

  /**
   * <p>
   * Returns the value which best matches one of the specified language ranges.
   * </p>
   * <p>
   * The return value could be:
   * </p>
   * <ol>
   * <li>Best matching locale key using the lookup mechanism defined in RFC 4647</li>
   * <li>Locale with undefined language key</li>
   * <li>First value in language map</li>
   * <li>null</li>
   * </ol>
   * <p>
   * <strong>Note</strong>: This helper method is not defined in the xAPI specification.
   * </p>
   *
   * @param languageRanges used for matching a locale key
   *
   * @return the value which best matches one of the specified language ranges, or null if this map
   *         is empty
   */
  public String get(List<LanguageRange> languageRanges) {

    // Return best match
    final var best = Locale.lookup(languageRanges, keySet());
    if (best != null) {
      return get(best);
    }

    // Otherwise return UND
    final var und = get(UNDEFINED_LOCALE);
    if (und != null) {
      return und;
    }

    // Otherwise return first
    final var iterator = keySet().iterator();
    if (iterator.hasNext()) {
      return get(iterator.next());
    }

    // Map must be empty
    return null;

  }

}
