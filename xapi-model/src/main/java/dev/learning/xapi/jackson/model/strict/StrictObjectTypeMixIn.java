/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson.model.strict;

import dev.learning.xapi.jackson.StrictObjectTypeResolverBuilder;
import tools.jackson.databind.annotation.JsonTypeResolver;

/**
 * StrictObjectTypeMixIn which adds StrictObjectTypeResolverBuilder.
 *
 * @author Thomas Turrell-Croft
 */
@JsonTypeResolver(StrictObjectTypeResolverBuilder.class)
public interface StrictObjectTypeMixIn {}
