/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.time.Clock;
import java.time.Instant;
import java.util.TreeMap;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** Tracks the retrieval boundary for this single-instance, synchronously persisted sample. */
@Component
public class StatementConsistency {

  private final Clock clock;
  private final TreeMap<Instant, Integer> pending = new TreeMap<>();
  private Instant observed = Instant.MIN;

  /** Creates a tracker using the UTC system clock. */
  public StatementConsistency() {
    this(Clock.systemUTC());
  }

  StatementConsistency(Clock clock) {
    this.clock = clock;
  }

  synchronized Instant consistentThrough() {
    final var now = observeTime();
    return pending.isEmpty() ? now : pending.firstKey();
  }

  <T> T write(Function<Instant, T> operation) {
    final var stored = begin();
    boolean deferred = false;
    try {
      // Repository methods normally commit before returning. If a caller owns a transaction,
      // retain its pending timestamp until that transaction has committed or rolled back.
      if (TransactionSynchronizationManager.isSynchronizationActive()) {
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
              @Override
              public void afterCompletion(int status) {
                complete(stored);
              }
            });
        deferred = true;
      }
      return operation.apply(stored);
    } finally {
      if (!deferred) {
        complete(stored);
      }
    }
  }

  private synchronized Instant begin() {
    final var stored = observeTime();
    pending.merge(stored, 1, Integer::sum);
    return stored;
  }

  private synchronized void complete(Instant stored) {
    pending.compute(stored, (time, count) -> count == 1 ? null : count - 1);
  }

  private Instant observeTime() {
    final var now = clock.instant();
    if (now.isAfter(observed)) {
      observed = now;
    }
    return observed;
  }
}
