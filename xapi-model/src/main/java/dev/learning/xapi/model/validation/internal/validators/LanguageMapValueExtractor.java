/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import dev.learning.xapi.model.LanguageMap;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.ValueExtractor;
import java.util.Locale;

/**
 * ValueExtractor for {@link LanguageMap}.
 *
 * @author István Rátkai (Selindek)
 */
@UnwrapByDefault
public class LanguageMapValueExtractor
    implements ValueExtractor<@ExtractedValue(type = Locale.class) LanguageMap> {

  @Override
  public void extractValues(LanguageMap originalValue, ValueReceiver receiver) {
    originalValue.keySet().forEach(k -> receiver.iterableValue(k.toLanguageTag(), k));
  }

}
