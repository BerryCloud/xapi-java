/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.Attachment;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.StatementResult;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.Part;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link HttpMessageReader} for reading {@code "multipart/mixed"} responses into a
 * {@link Statement} or {@link StatementResult}s object.
 *
 * @author István Rátkai (Selindek)
 */
public class StatementHttpMessageReader extends LoggingCodecSupport
    implements HttpMessageReader<Object> {

  static final List<MediaType> MIME_TYPES = List.of(MediaType.MULTIPART_MIXED);

  private final HttpMessageReader<Part> partReader = new DefaultPartHttpMessageReader();

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MediaType> getReadableMediaTypes() {
    return MIME_TYPES;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
    if (Statement.class.equals(elementType.toClass())
        || StatementResult.class.equals(elementType.toClass())) {
      if (mediaType == null) {
        return true;
      }
      for (final MediaType supportedMediaType : MIME_TYPES) {
        if (supportedMediaType.isCompatibleWith(mediaType)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Flux<Object> read(ResolvableType elementType, ReactiveHttpInputMessage message,
      Map<String, Object> hints) {

    return Flux.from(readMono(elementType, message, hints));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Mono<Object> readMono(ResolvableType elementType, ReactiveHttpInputMessage inputMessage,
      Map<String, Object> hints) {

    return this.partReader.read(elementType, inputMessage, hints).collectList()
        .flatMap(list -> toStatement(elementType, list));
  }

  private Mono<Object> toStatement(ResolvableType elementType, List<Part> parts) {

    if (parts.isEmpty()) {
      return null;
    }

    final var jsonPart = parts.get(0);
    final var jsonType = jsonPart.headers().getContentType();

    if (!MediaType.APPLICATION_JSON.isCompatibleWith(jsonType)) {
      return null;
    }

    // Create a virtual response from the the first (json) part...
    final var jsonResponse = ClientResponse.create(HttpStatusCode.valueOf(200))
        .body(jsonPart.content()).headers(headers -> headers.addAll(jsonPart.headers())).build();

    // ... and use the default extractors to extract its content to a Statement/StatementResult
    Flux<Object> partDataFlux = jsonResponse.bodyToFlux(elementType.toClass());

    // merge the attachment parts contents with it
    for (var i = 1; i < parts.size(); i++) {
      partDataFlux = partDataFlux.mergeWith(parts.get(i).content());
    }

    // Now we have direct access to all the data
    return partDataFlux.collectList().map(partData -> {
      // the first part's data is the Statement / StatementResult
      final var object = partData.get(0);
      final var statements = object instanceof final Statement statement ? Arrays.asList(statement)
          : ((StatementResult) object).getStatements();

      for (var i = 1; i < partData.size(); i++) {
        final var buffer = (DataBuffer) partData.get(i);
        final var content = new byte[buffer.readableByteCount()];
        buffer.read(content);
        DataBufferUtils.release(buffer);
        final var sha2 = parts.get(i).headers().getFirst("X-Experience-API-Hash");
        injectAttachmentContent(statements, sha2, content);
      }

      return object;
    });

  }

  /**
   * Inject the content into each {@link Attachment} in each statements with the matching sha2.
   */
  private void injectAttachmentContent(List<Statement> statements, String sha2, byte[] content) {
    for (final var statement : statements) {
      final var attachments = statement.getAttachments();
      if (attachments != null) {
        final var size = attachments.size();
        for (var i = 0; i < size; i++) {
          final var attachment = attachments.get(i);
          if (sha2.equals(attachment.getSha2())) {
            attachments.set(i, attachment.withContent(content));
          }
        }
      }
    }
  }

}
