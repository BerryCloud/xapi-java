/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.Attachment;
import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.multipart.MultipartWriterSupport;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;

/**
 * {@link HttpMessageWriter} for writing {@link Attachment} data into multipart/mixed requests.
 *
 * @author István Rátkai (Selindek)
 */
public class AttachmentHttpMessageWriter extends MultipartWriterSupport
    implements HttpMessageWriter<Attachment> {

  public AttachmentHttpMessageWriter() {
    super(List.of(MediaType.MULTIPART_MIXED));
  }

  @Override
  public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
    return Attachment.class.isAssignableFrom(elementType.toClass());
  }

  @Override
  public Mono<Void> write(Publisher<? extends Attachment> parts, ResolvableType elementType,
      @Nullable MediaType mediaType, ReactiveHttpOutputMessage outputMessage,
      Map<String, Object> hints) {

    return Mono.from(parts).flatMap(part -> {
      // set attachment part headers
      outputMessage.getHeaders().setContentType(MediaType.valueOf(part.getContentType()));
      outputMessage.getHeaders().set("Content-Transfer-Encoding", "binary");
      outputMessage.getHeaders().set("X-Experience-API-Hash", part.getSha2());

      // write attachment content
      return outputMessage.writeWith(encodePart(part, outputMessage.bufferFactory()));
    }).doOnDiscard(DataBuffer.class, DataBufferUtils::release);

  }

  private Mono<DataBuffer> encodePart(Attachment part, DataBufferFactory bufferFactory) {

    return Mono.fromCallable(() -> {
      final var buffer = bufferFactory.allocateBuffer(part.getContent().length);
      buffer.write(part.getContent());
      return buffer;
    });

  }

}
