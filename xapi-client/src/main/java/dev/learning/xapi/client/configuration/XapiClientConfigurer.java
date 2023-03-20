/*
 * Copyright 2016-2019 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client.configuration;

import dev.learning.xapi.client.XapiClient;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Interface for {@link XapiClient} configurers.
 * <p>
 * {@link XapiClient} is basically an extension of {@link WebClient}, so all custom configuration
 * can be done via a {@link Builder} object.
 * </p>
 *
 * @author István Rátkai (Selindek)
 */
public interface XapiClientConfigurer extends Consumer<WebClient.Builder> {

}
