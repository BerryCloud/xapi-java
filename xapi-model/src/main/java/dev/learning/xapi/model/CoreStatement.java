/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

/**
 * This is a helper interface for signing Statement-like objects which can be targets of class level
 * Statement-validators.
 *
 * @author István Rátkai (Selindek)
 */
public interface CoreStatement {

  Actor getActor();

  Verb getVerb();

  Object getObject();

  Context getContext();

}
