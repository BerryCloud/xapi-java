/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import java.time.Instant;
import java.util.List;

/**
 * Contains the methods that are in common between Statement and SubStatement.
 *
 * @author István Rátkai (Selindek)
 */
public interface CoreStatement {

  /**
   * Whom the Statement is about, as an Agent or Group Object.
   */
  Actor getActor();

  /**
   * Action taken by the Actor.
   */
  Verb getVerb();

  /**
   * Activity, Agent, or another Statement that is the Object of the Statement.
   */
  Object getObject();

  /**
   * Result Object, further details representing a measured outcome.
   */
  Result getResult();

  /**
   * Context that gives the Statement more meaning.
   */
  Context getContext();

  /**
   * Timestamp of when the events described within this Statement occurred.
   */
  Instant getTimestamp();

  /**
   * Headers for Attachments to the Statement.
   */
  List<Attachment> getAttachments();

}
