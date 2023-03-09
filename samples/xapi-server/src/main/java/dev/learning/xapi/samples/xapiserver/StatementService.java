/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.learning.xapi.model.Statement;

/**
 * StatementService.
 * 
 * @author István Rátkai (Selindek)
 *
 */
@Service
public class StatementService {

  Logger log = LoggerFactory.getLogger(StatementController.class);
  
  /**
   * Processes an incoming statement
   * @param statementId the id of the statement
   * @param statement the Statement itself
   */
  public void processStatement(UUID statementId, Statement statement) {
   
    log.info("processing statement: {}", statement);
    
    // add custom logic here...
  }

}
