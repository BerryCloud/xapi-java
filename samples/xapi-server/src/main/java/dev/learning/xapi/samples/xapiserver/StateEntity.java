/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A state document and its retrieval metadata. */
@Entity
@Table(indexes = @Index(name = "state_scope_registration", columnList = "scope,registration"))
@Getter
@Setter
@NoArgsConstructor
public class StateEntity {

  @EmbeddedId private StateKey id;

  @Lob
  @Column(nullable = false)
  private byte[] contents;

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private Instant updated;

  @Version private Long version;
}
