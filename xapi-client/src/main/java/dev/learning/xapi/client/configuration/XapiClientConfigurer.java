/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client.configuration;

import dev.learning.xapi.client.XapiClient;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Interface for {@link XapiClient} a configurer.
 * <p>
 * Custom configuration of the XapiClient can be done via a {@link WebClient.Builder} object.
 * </p>
 *
 * @author István Rátkai (Selindek)
 */
@Component
public interface XapiClientConfigurer extends Consumer<WebClient.Builder> {

}
