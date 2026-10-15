/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

class StatementConsistencyTest {

  private static final Instant START = Instant.parse("2026-10-15T12:00:00Z");
  private final MutableClock clock = new MutableClock();
  private final StatementConsistency consistency = new StatementConsistency(clock);

  @Test
  void idleBoundaryAdvancesButClockRegressionCannotBackdateFutureWrites() {
    assertEquals(START, consistency.consistentThrough());
    clock.now = START.plusSeconds(10);
    assertEquals(clock.now, consistency.consistentThrough());
    clock.now = START.minusSeconds(10);
    assertEquals(START.plusSeconds(10), consistency.consistentThrough());
    consistency.write(
        stored -> {
          assertEquals(START.plusSeconds(10), stored);
          return null;
        });
  }

  @Test
  void laterWriteCanFinishWithoutAdvancingPastAnEarlierPendingWrite() throws Exception {
    final var started = new CountDownLatch(1);
    final var finish = new CountDownLatch(1);
    try (var executor = Executors.newSingleThreadExecutor()) {
      final var first =
          executor.submit(
              () ->
                  consistency.write(
                      stored -> {
                        started.countDown();
                        try {
                          assertTrue(finish.await(5, TimeUnit.SECONDS));
                        } catch (InterruptedException e) {
                          Thread.currentThread().interrupt();
                          throw new IllegalStateException(e);
                        }
                        return stored;
                      }));
      try {
        assertTrue(started.await(5, TimeUnit.SECONDS));
        clock.now = START.plusSeconds(1);
        consistency.write(
            stored -> {
              assertEquals(clock.now, stored);
              return null;
            });
        assertEquals(START, consistency.consistentThrough());
      } finally {
        finish.countDown();
      }
      assertEquals(START, first.get(5, TimeUnit.SECONDS));
      assertEquals(clock.now, consistency.consistentThrough());
    }
  }

  @Test
  void writesWithIdenticalTimestampsAreTrackedSeparately() {
    consistency.write(
        first -> {
          consistency.write(second -> null);
          clock.now = START.plusSeconds(1);
          assertEquals(START, consistency.consistentThrough());
          return null;
        });
    assertEquals(clock.now, consistency.consistentThrough());
  }

  @Test
  void failedWriteDoesNotLeaveTheBoundaryStuck() {
    assertThrows(
        IllegalStateException.class,
        () ->
            consistency.write(
                stored -> {
                  clock.now = START.plusSeconds(1);
                  assertEquals(START, consistency.consistentThrough());
                  throw new IllegalStateException("Storage failed");
                }));
    assertEquals(clock.now, consistency.consistentThrough());
  }

  @Test
  void enclosingTransactionKeepsWritePendingUntilCommitOrRollback() {
    for (int status :
        new int[] {
          TransactionSynchronization.STATUS_COMMITTED, TransactionSynchronization.STATUS_ROLLED_BACK
        }) {
      clock.now = START;
      final var tracker = new StatementConsistency(clock);
      TransactionSynchronizationManager.initSynchronization();
      try {
        tracker.write(stored -> null);
        clock.now = START.plusSeconds(1);
        assertEquals(START, tracker.consistentThrough());
        TransactionSynchronizationManager.getSynchronizations()
            .forEach(synchronization -> synchronization.afterCompletion(status));
        assertEquals(clock.now, tracker.consistentThrough());
      } finally {
        TransactionSynchronizationManager.clearSynchronization();
      }
    }
  }

  private static class MutableClock extends Clock {
    private volatile Instant now = START;

    @Override
    public ZoneId getZone() {
      return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
      return Clock.fixed(now, zone);
    }

    @Override
    public Instant instant() {
      return now;
    }
  }
}
