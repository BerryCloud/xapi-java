/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Type;

/**
 * StatementEntity.
 *
 * @author Thomas Turrell-Croft
 */
@Entity
public class StatementEntity {

  @Id
  private UUID id;

  @Type(JsonType.class)
  @Column(columnDefinition = "BLOB")
  private JsonNode statement;

  @Column(nullable = false)
  private Instant stored;

  /**
   * StatementEntity Constructor.
   */
  public StatementEntity() {}

  /**
   * StatementEntity Constructor.
   *
   * @param id the statement id
   * @param statement the statement as JSON
   * @param stored the stored timestamp
   * @param stored the stored timestamp
   */
  public StatementEntity(UUID id, JsonNode statement, Instant stored) {

    this.id = id;
    this.statement = statement;
    this.stored = stored;

  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the id to set
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Gets the statement.
   *
   * @return the statement
   */
  public JsonNode getStatement() {
    return statement;
  }

  /**
   * Sets the statement.
   *
   * @param statement the statement to set
   */
  public void setStatement(JsonNode statement) {
    this.statement = statement;
  }

  /**
   * Gets the stored timestamp.
   *
   * @return the stored timestamp
   */
  public Instant getStored() {
    return stored;
  }

  /**
   * Sets the stored timestamp.
   *
   * @param stored the stored timestamp to set
   */
  public void setStored(Instant stored) {
    this.stored = stored;
  }

}
