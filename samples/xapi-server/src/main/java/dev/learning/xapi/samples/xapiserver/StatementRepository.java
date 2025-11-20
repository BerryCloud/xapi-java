/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Statement Repository.
 *
 * @author Thomas Turrell-Croft
 */
public interface StatementRepository extends PagingAndSortingRepository<StatementEntity, UUID> {

  Slice<StatementEntity> findAllByOrderByStoredAscIdAsc(Pageable pageable);

  Slice<StatementEntity> findByStoredGreaterThanEqualOrderByStoredAscIdAsc(Instant stored,
      Pageable pageable);


}
