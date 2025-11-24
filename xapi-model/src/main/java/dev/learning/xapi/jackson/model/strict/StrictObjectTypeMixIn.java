/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson.model.strict;

import tools.jackson.databind.annotation.JsonTypeResolver;
import dev.learning.xapi.jackson.StrictObjectTypeResolverBuilder;

/**
 * StrictObjectTypeMixIn which adds StrictObjectTypeResolverBuilder.
 *
 * @author Thomas Turrell-Croft
 */
@JsonTypeResolver(StrictObjectTypeResolverBuilder.class)
public interface StrictObjectTypeMixIn {}
