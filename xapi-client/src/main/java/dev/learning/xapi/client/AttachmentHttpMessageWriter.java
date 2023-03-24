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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.multipart.MultipartWriterSupport;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
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
    if (Attachment.class.isAssignableFrom(elementType.toClass())) {
      if (mediaType == null) {
        return true;
      }
      System.err.println("  attachment mediaType: " + mediaType);
      for (final MediaType supportedMediaType : getWritableMediaTypes()) {
        if (supportedMediaType.isCompatibleWith(mediaType)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Mono<Void> write(Publisher<? extends Attachment> parts, ResolvableType elementType,
      @Nullable MediaType mediaType, ReactiveHttpOutputMessage outputMessage,
      Map<String, Object> hints) {

    final var headers = new HttpHeaders();

    final Flux<DataBuffer> body = Flux.from(parts)

        .doOnNext(part -> {
          // outputMessage.getHeaders().setContentType(MediaType.valueOf(part.getContentType()));
          // outputMessage.getHeaders().set("Content-Transfer-Encoding", "binary");
          // outputMessage.getHeaders().set("X-Experience-API-Hash", part.getSha2());
        })

        .concatMap(part -> encodePart(headers, part, outputMessage.bufferFactory()))

        // .concatWith(generateLastLine(boundary, outputMessage.bufferFactory()))
        .doOnDiscard(DataBuffer.class, DataBufferUtils::release);


    return body.singleOrEmpty().flatMap(buffer -> {
      outputMessage.getHeaders().addAll(headers);
      return outputMessage
          .writeWith(Mono.just(buffer).doOnDiscard(DataBuffer.class, DataBufferUtils::release));
    }).doOnDiscard(DataBuffer.class, DataBufferUtils::release);


    // return outputMessage.writeWith(body);

  }

  private Flux<DataBuffer> encodePart(HttpHeaders headers, Attachment part,
      DataBufferFactory bufferFactory) {
    headers.setContentType(MediaType.valueOf(part.getContentType()));
    headers.set("Content-Transfer-Encoding", "binary");
    headers.set("X-Experience-API-Hash", part.getSha2());

    return Flux.concat(

        // Mono.fromCallable(() -> {
        // final var buffer = bufferFactory.allocateBuffer(part.getContent().length);
        // buffer.write(part.getContent());
        // return buffer;
        // }),

        Mono.fromCallable(() -> {
          final var buffer = bufferFactory.allocateBuffer(part.getContent().length);
          buffer.write(part.getContent());
          return buffer;
        })

    );
  }

}
