/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

/**
 * StatementEntity.
 *
 * @author Thomas Turrell-Croft
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatementEntity {

  @Id private UUID id;

  @Type(JsonType.class)
  @Column(columnDefinition = "BLOB")
  private JsonNode statement;
}
