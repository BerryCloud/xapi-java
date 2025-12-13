/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.Attachment;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.SubStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CodecException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.multipart.MultipartHttpMessageWriter;
import org.springframework.http.codec.multipart.MultipartWriterSupport;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link HttpMessageWriter} for writing a {@link Statement} or list of Statements.
 *
 * <p>If any of the provided statements contains an {@link Attachment} with real data, then this
 * writer creates a multipart/mixed output otherwise it writes the data as application/json.
 *
 * <p>This message-writer accepts <strong>ALL</strong> objects, so all the default (and any other
 * custom) {@link HttpMessageWriter} must be passed to its constructor. If the object to be written
 * is not a {@link Statement} or List of Statements with real {@link Attachment}s, then this list of
 * writers will be used.
 *
 * @author István Rátkai (Selindek)
 * @see AttachmentHttpMessageWriter
 */
public class StatementHttpMessageWriter extends MultipartWriterSupport
    implements HttpMessageWriter<Object> {

  private final List<HttpMessageWriter<?>> writers = new ArrayList<>();

  /**
   * Constructor.
   *
   * @param list list of the original {@link HttpMessageWriter}s. This list is used if the object to
   *     write is not a {@link Statement} or list of statements or there are no any {@link
   *     Attachment}s with real data in the statements.
   */
  public StatementHttpMessageWriter(List<HttpMessageWriter<?>> list) {

    super(List.of(MediaType.MULTIPART_MIXED, MediaType.APPLICATION_JSON));

    // Add special writer for attachments
    this.writers.add(new AttachmentHttpMessageWriter());
    // ... but otherwise use the default list of writers
    this.writers.addAll(list);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("unchecked")
  public Mono<Void> write(
      Publisher<? extends Object> inputStream,
      ResolvableType elementType,
      @Nullable MediaType mediaType,
      ReactiveHttpOutputMessage outputMessage,
      Map<String, Object> hints) {

    return Mono.from(inputStream)
        .flatMap(
            object -> {
              final var list = getParts(object);
              if (list.size() > 1) {
                // Has attachments -> process as multipart
                return writeMultipart(list, outputMessage, hints);

              } else {
                // No attachments -> pass the original object to the default list of writers

                return ((HttpMessageWriter<Object>)
                        writers.stream()
                            .filter(partWriter -> partWriter.canWrite(elementType, mediaType))
                            .findFirst()
                            .get())
                    .write(inputStream, elementType, mediaType, outputMessage, hints);
              }
            });
  }

  private Mono<Void> writeMultipart(
      List<Object> list, ReactiveHttpOutputMessage outputMessage, Map<String, Object> hints) {

    final var boundary = generateMultipartBoundary();

    final var mediaType = getMultipartMediaType(MediaType.MULTIPART_MIXED, boundary);
    outputMessage.getHeaders().setContentType(mediaType);

    final var bufferFactory = outputMessage.bufferFactory();

    final Flux<DataBuffer> body =
        Flux.fromIterable(list)
            .concatMap(element -> encodePart(boundary, element, bufferFactory, hints))
            .concatWith(generateLastLine(boundary, bufferFactory))
            .doOnDiscard(DataBuffer.class, DataBufferUtils::release);

    return outputMessage.writeWith(body);
  }

  @SuppressWarnings("unchecked")
  private <T> Flux<DataBuffer> encodePart(
      byte[] boundary, Object body, DataBufferFactory factory, Map<String, Object> hints) {
    final var message = new MultipartHttpOutputMessage(factory);
    final var headers = message.getHeaders();

    final var resolvableType = ResolvableType.forClass(body.getClass());

    final var contentType = headers.getContentType();

    final var writer =
        this.writers.stream()
            .filter(partWriter -> partWriter.canWrite(resolvableType, contentType))
            .findFirst();

    if (!writer.isPresent()) {
      return Flux.error(
          new CodecException("No suitable writer found for part: " + resolvableType.toClass()));
    }

    final var bodyPublisher = body instanceof Publisher ? (Publisher<T>) body : Mono.just(body);

    // The writer will call MultipartHttpOutputMessage#write which doesn't actually write
    // but only stores the body Flux and returns Mono.empty().

    final var partContentReady =
        ((HttpMessageWriter<Object>) writer.get())
            .write(bodyPublisher, resolvableType, contentType, message, hints);

    // After partContentReady, we can access the part content from MultipartHttpOutputMessage
    // and use it for writing to the actual request body

    final Flux<DataBuffer> partContent = partContentReady.thenMany(Flux.defer(message::getBody));

    return Flux.concat(
        generateBoundaryLine(boundary, factory), partContent, generateNewLine(factory));
  }

  @SuppressWarnings("unchecked")
  private List<Object> getParts(Object object) {

    final var list = new ArrayList<>();

    final Stream<Attachment> attachments;
    
    if (object instanceof Statement statement) {
      attachments = getRealAttachments(statement);
    } else if (object instanceof List<?> statements
        && !statements.isEmpty() && statements.get(0) instanceof Statement) {
      attachments = ((List<Statement>) statements).stream().flatMap(this::getRealAttachments);
    } else {
      attachments = null;
    }

    if (attachments == null) {
      // The object is not a statement or list of statements
      return list;
    }

    // first part is the statement / list of statements
    list.add(object);

    list.addAll(attachments.distinct().toList());
    return list;
  }

  /**
   * Gets {@link Attachment}s of a {@link Statement} which has data property as a {@link Stream}.
   *
   * @param statement a {@link Statement} object
   * @return {@link Attachment} of a {@link Statement} which has data property as a {@link Stream}.
   */
  private Stream<Attachment> getRealAttachments(Statement statement) {

    Stream<Attachment> stream;

    // handle the rare scenario when a sub-statement has an attachment
    if (statement.getObject() instanceof final SubStatement substatement
        && substatement.getAttachments() != null) {
      stream = substatement.getAttachments().stream();
    } else {
      stream = Stream.empty();
    }

    if (statement.getAttachments() != null) {
      stream = Stream.concat(stream, statement.getAttachments().stream());
    }

    return stream.filter(a -> a.getContent() != null);
  }

  /**
   * This class was copied from the {@link MultipartHttpMessageWriter} class. Unfortunately it's a
   * private class, so I cannot use it directly.
   */
  private class MultipartHttpOutputMessage implements ReactiveHttpOutputMessage {

    private final DataBufferFactory bufferFactory;

    private final HttpHeaders headers = new HttpHeaders();

    private final AtomicBoolean committed = new AtomicBoolean();

    @Nullable private Flux<DataBuffer> body;

    public MultipartHttpOutputMessage(DataBufferFactory bufferFactory) {
      this.bufferFactory = bufferFactory;
    }

    @Override
    public HttpHeaders getHeaders() {
      return this.body != null ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
    }

    @Override
    public DataBufferFactory bufferFactory() {
      return this.bufferFactory;
    }

    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {
      this.committed.set(true);
    }

    @Override
    public boolean isCommitted() {
      return this.committed.get();
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
      if (this.body != null) {
        return Mono.error(new IllegalStateException("Multiple calls to writeWith() not supported"));
      }
      this.body = generatePartHeaders(this.headers, this.bufferFactory).concatWith(body);

      // We don't actually want to write (just save the body Flux)
      return Mono.empty();
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
      return Mono.error(new UnsupportedOperationException());
    }

    public Flux<DataBuffer> getBody() {
      return this.body != null
          ? this.body
          : Flux.error(new IllegalStateException("Body has not been written yet"));
    }

    @Override
    public Mono<Void> setComplete() {
      return Mono.error(new UnsupportedOperationException());
    }
  }
}
