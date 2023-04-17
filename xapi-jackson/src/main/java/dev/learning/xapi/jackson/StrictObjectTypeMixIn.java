/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;

/**
 * StrictObjectTypeMixIn which adds StrictObjectTypeResolverBuilder.
 *
 * @author Thomas Turrell-Croft
 */
@JsonTypeResolver(StrictObjectTypeResolverBuilder.class)
public interface StrictObjectTypeMixIn {
}
