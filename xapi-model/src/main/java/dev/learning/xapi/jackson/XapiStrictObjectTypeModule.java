/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.learning.xapi.jackson.model.strict.StrictObjectTypeMixIn;
import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Actor;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Group;
import dev.learning.xapi.model.StatementObject;
import dev.learning.xapi.model.StatementReference;
import dev.learning.xapi.model.SubStatement;
import dev.learning.xapi.model.SubStatementObject;

/**
 * xAPI JSON module for enabling strict ObjectTypeResolver.
 *
 * @author István Rátkai (Selindek)
 */
public class XapiStrictObjectTypeModule extends SimpleModule {

  private static final long serialVersionUID = -5943467400927276326L;

  /** XapiStrictObjectTypeModule constructor. */
  public XapiStrictObjectTypeModule() {
    super("xAPI Strict ObjectType Module");

    setMixInAnnotation(Agent.class, StrictObjectTypeMixIn.class);
    setMixInAnnotation(Actor.class, StrictObjectTypeMixIn.class);
    setMixInAnnotation(Group.class, StrictObjectTypeMixIn.class);
    setMixInAnnotation(Activity.class, StrictObjectTypeMixIn.class);
    setMixInAnnotation(StatementObject.class, StrictObjectTypeMixIn.class);
    setMixInAnnotation(SubStatement.class, StrictObjectTypeMixIn.class);
    setMixInAnnotation(StatementReference.class, StrictObjectTypeMixIn.class);
    setMixInAnnotation(SubStatementObject.class, StrictObjectTypeMixIn.class);
  }
}
