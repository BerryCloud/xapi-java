/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Map;
import java.util.Optional;

/**
 * A language map is a dictionary where the key is a RFC 5646 Language Tag, and the value is a
 * string in the language specified in the tag.
 *
 * @author Thomas Turrell-Croft
 * 
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#lang-maps">Language
 *      Maps</a>
 */
public class LanguageMap extends LinkedHashMap<Locale, String> {

  private static final long serialVersionUID = 7375610804995032187L;

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
    this.put(new Locale("und"), value);
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
    final Locale best = Locale.lookup(languageRanges, keySet());
    if (best != null) {
      return get(best);
    }

    // Otherwise return UND
    final String und = get(new Locale("und"));
    if (und != null) {
      return und;
    }

    // Otherwise return first
    final Optional<Locale> first = keySet().stream().findFirst();
    if (first.isPresent()) {
      return get(first.get());
    }

    // Map must be empty
    return null;

  }

}
