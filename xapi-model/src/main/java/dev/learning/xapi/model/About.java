/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import dev.learning.xapi.model.validation.constraints.HasScheme;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI About object.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 * 
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#24-agents-resource">xAPI
 *      About</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class About {

  private List<String> version;

  private LinkedHashMap<@HasScheme URI, Object> extensions;

  // **Warning** do not add fields that are not required by the xAPI specification.

}
