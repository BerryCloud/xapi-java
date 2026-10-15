/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A state document and its retrieval metadata. */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class StateEntity {

  @Id private String id;

  @Column(nullable = false)
  private String scope;

  private String registration;

  @Lob
  @Column(nullable = false)
  private String stateId;

  @Lob
  @Column(nullable = false)
  private byte[] contents;

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private Instant updated;

  @Version private Long version;
}
