/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.client.PostStatementsRequest.StatementList;
import dev.learning.xapi.model.Attachment;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.SubStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.multipart.MultipartWriterSupport;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link HttpMessageWriter} for writing a {@link Statement} or {@link StatementList}
 * <p>
 * If any of the provided statements contains an {@link Attachment} with real data, then this writer
 * creates a multipart/mixed output otherwise it writes the data as application/json.
 * </p>
 *
 * @author István Rátkai (Selindek)
 *
 * @see AttachmentHttpMessageWriter
 */
public class StatementHttpMessageWriter extends MultipartWriterSupport
    implements HttpMessageWriter<Object> {

  /** Suppress logging from individual part writers (full map logged at this level). */
  private static final Map<String, Object> DEFAULT_HINTS =
      Hints.from(Hints.SUPPRESS_LOGGING_HINT, true);

  private final List<HttpMessageWriter<?>> partWritersSupplier;
  private final HttpMessageWriter<Object> defaultWriter;

  public StatementHttpMessageWriter() {

    super(List.of(MediaType.MULTIPART_MIXED, MediaType.APPLICATION_JSON));
    this.partWritersSupplier = Arrays.asList(new AttachmentHttpMessageWriter(),
        new EncoderHttpMessageWriter<>(new Jackson2JsonEncoder()));
    this.defaultWriter = new EncoderHttpMessageWriter<>(new Jackson2JsonEncoder());
  }

  @Override
  public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
    return Statement.class.equals(elementType.toClass())
        || StatementList.class.isAssignableFrom(elementType.toClass());
  }

  @Override
  public Mono<Void> write(Publisher<? extends Object> inputStream, ResolvableType elementType,
      @Nullable MediaType mediaType, ReactiveHttpOutputMessage outputMessage,
      Map<String, Object> hints) {

    return Mono.from(inputStream).flatMap(object -> {
      final var list = getParts(object);
      if (list.size() > 1) {
        // Has attachments
        return writeMultipart(list, outputMessage, hints);
      } else {
        // No attachments
        final Mono<Object> input = Mono.just(list.get(0));
        return this.defaultWriter.write(input, elementType, mediaType, outputMessage, hints);
      }
    });
  }

  private Mono<Void> writeMultipart(List<Object> list, ReactiveHttpOutputMessage outputMessage,
      Map<String, Object> hints) {

    final var boundary = generateMultipartBoundary();

    final var mediaType = getMultipartMediaType(MediaType.MULTIPART_MIXED, boundary);
    outputMessage.getHeaders().setContentType(mediaType);

    final var bufferFactory = outputMessage.bufferFactory();

    final Flux<DataBuffer> body =
        Flux.fromIterable(list).concatMap(element -> encodePart(boundary, element, bufferFactory))
            .concatWith(generateLastLine(boundary, bufferFactory))
            .doOnDiscard(DataBuffer.class, DataBufferUtils::release);

    return outputMessage.writeWith(body);
  }

  @SuppressWarnings("unchecked")
  private <T> Flux<DataBuffer> encodePart(byte[] boundary, Object body, DataBufferFactory factory) {
    final var message = new MultipartHttpOutputMessage(factory);
    final var headers = message.getHeaders();

    final var resolvableType = ResolvableType.forClass(body.getClass());

    final var contentType = headers.getContentType();

    final var writer = this.partWritersSupplier.stream()
        .filter(partWriter -> partWriter.canWrite(resolvableType, contentType)).findFirst();

    if (!writer.isPresent()) {
      return Flux.error(
          new CodecException("No suitable writer found for part: " + resolvableType.toClass()));
    }

    final var bodyPublisher = body instanceof Publisher ? (Publisher<T>) body : Mono.just(body);

    // The writer will call MultipartHttpOutputMessage#write which doesn't actually write
    // but only stores the body Flux and returns Mono.empty().

    final var partContentReady = ((HttpMessageWriter<Object>) writer.get()).write(bodyPublisher,
        resolvableType, contentType, message, DEFAULT_HINTS);

    // After partContentReady, we can access the part content from MultipartHttpOutputMessage
    // and use it for writing to the actual request body

    final Flux<DataBuffer> partContent = partContentReady.thenMany(Flux.defer(message::getBody));

    return Flux.concat(generateBoundaryLine(boundary, factory), partContent,
        generateNewLine(factory));
  }

  private class MultipartHttpOutputMessage implements ReactiveHttpOutputMessage {

    private final DataBufferFactory bufferFactory;

    private final HttpHeaders headers = new HttpHeaders();

    private final AtomicBoolean committed = new AtomicBoolean();

    @Nullable
    private Flux<DataBuffer> body;

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
      return this.body != null ? this.body
          : Flux.error(new IllegalStateException("Body has not been written yet"));
    }

    @Override
    public Mono<Void> setComplete() {
      return Mono.error(new UnsupportedOperationException());
    }
  }

  private List<Object> getParts(Object object) {
    final var list = new ArrayList<>();

    // first part is the statement / list of statements
    list.add(object);

    Stream<Attachment> attachments;
    if (object instanceof final Statement statement) {
      attachments = getRealAttachments(statement);
    } else {
      attachments = ((StatementList) object).stream().flatMap(this::getRealAttachments);
    }

    list.addAll(attachments.distinct().toList());
    return list;
  }

  /**
   * Gets {@link Attachment}s of a {@link Statement} which has data property as a {@link Stream}.
   *
   * @param statement a {@link Statement} object
   *
   * @return {@link Attachment} of a {@link Statement} which has data property as a {@link Stream}.
   */
  private Stream<Attachment> getRealAttachments(Statement statement) {

    // handle the rare scenario when a sub-statement has an attachment
    var stream = statement.getObject() instanceof final SubStatement substatement
        && substatement.getAttachments() != null ? substatement.getAttachments().stream()
            : Stream.<Attachment>empty();

    if (statement.getAttachments() != null) {
      stream = Stream.concat(stream, statement.getAttachments().stream());
    }

    return stream.filter(a -> a.getContent() != null);
  }

}
