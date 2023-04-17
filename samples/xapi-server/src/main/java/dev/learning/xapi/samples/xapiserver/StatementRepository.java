/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Statement Repository.
 *
 * @author Thomas Turrell-Croft
 */
public interface StatementRepository extends CrudRepository<StatementEntity, UUID> {


}
