package edu.rutmiit.demo.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Сервисы
 * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.66.0)",
    comments = "Source: book_analytics.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BookAnalyticsGrpc {

  private BookAnalyticsGrpc() {}

  public static final java.lang.String SERVICE_NAME = "bookanalytics.BookAnalytics";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeBookRequest,
      edu.rutmiit.demo.grpc.BookAnalysisResponse> getAnalyzeBookMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AnalyzeBook",
      requestType = edu.rutmiit.demo.grpc.AnalyzeBookRequest.class,
      responseType = edu.rutmiit.demo.grpc.BookAnalysisResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeBookRequest,
      edu.rutmiit.demo.grpc.BookAnalysisResponse> getAnalyzeBookMethod() {
    io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeBookRequest, edu.rutmiit.demo.grpc.BookAnalysisResponse> getAnalyzeBookMethod;
    if ((getAnalyzeBookMethod = BookAnalyticsGrpc.getAnalyzeBookMethod) == null) {
      synchronized (BookAnalyticsGrpc.class) {
        if ((getAnalyzeBookMethod = BookAnalyticsGrpc.getAnalyzeBookMethod) == null) {
          BookAnalyticsGrpc.getAnalyzeBookMethod = getAnalyzeBookMethod =
              io.grpc.MethodDescriptor.<edu.rutmiit.demo.grpc.AnalyzeBookRequest, edu.rutmiit.demo.grpc.BookAnalysisResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AnalyzeBook"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.rutmiit.demo.grpc.AnalyzeBookRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.rutmiit.demo.grpc.BookAnalysisResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookAnalyticsMethodDescriptorSupplier("AnalyzeBook"))
              .build();
        }
      }
    }
    return getAnalyzeBookMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BookAnalyticsStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookAnalyticsStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookAnalyticsStub>() {
        @java.lang.Override
        public BookAnalyticsStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookAnalyticsStub(channel, callOptions);
        }
      };
    return BookAnalyticsStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BookAnalyticsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookAnalyticsBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookAnalyticsBlockingStub>() {
        @java.lang.Override
        public BookAnalyticsBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookAnalyticsBlockingStub(channel, callOptions);
        }
      };
    return BookAnalyticsBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BookAnalyticsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookAnalyticsFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookAnalyticsFutureStub>() {
        @java.lang.Override
        public BookAnalyticsFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookAnalyticsFutureStub(channel, callOptions);
        }
      };
    return BookAnalyticsFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Анализирует книгу и возвращает вычисленные метрики
     * </pre>
     */
    default void analyzeBook(edu.rutmiit.demo.grpc.AnalyzeBookRequest request,
        io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.BookAnalysisResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAnalyzeBookMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BookAnalytics.
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public static abstract class BookAnalyticsImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BookAnalyticsGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BookAnalytics.
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public static final class BookAnalyticsStub
      extends io.grpc.stub.AbstractAsyncStub<BookAnalyticsStub> {
    private BookAnalyticsStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookAnalyticsStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookAnalyticsStub(channel, callOptions);
    }

    /**
     * <pre>
     * Анализирует книгу и возвращает вычисленные метрики
     * </pre>
     */
    public void analyzeBook(edu.rutmiit.demo.grpc.AnalyzeBookRequest request,
        io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.BookAnalysisResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAnalyzeBookMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BookAnalytics.
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public static final class BookAnalyticsBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BookAnalyticsBlockingStub> {
    private BookAnalyticsBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookAnalyticsBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookAnalyticsBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Анализирует книгу и возвращает вычисленные метрики
     * </pre>
     */
    public edu.rutmiit.demo.grpc.BookAnalysisResponse analyzeBook(edu.rutmiit.demo.grpc.AnalyzeBookRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAnalyzeBookMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BookAnalytics.
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public static final class BookAnalyticsFutureStub
      extends io.grpc.stub.AbstractFutureStub<BookAnalyticsFutureStub> {
    private BookAnalyticsFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookAnalyticsFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookAnalyticsFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Анализирует книгу и возвращает вычисленные метрики
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.rutmiit.demo.grpc.BookAnalysisResponse> analyzeBook(
        edu.rutmiit.demo.grpc.AnalyzeBookRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAnalyzeBookMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ANALYZE_BOOK = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ANALYZE_BOOK:
          serviceImpl.analyzeBook((edu.rutmiit.demo.grpc.AnalyzeBookRequest) request,
              (io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.BookAnalysisResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getAnalyzeBookMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.rutmiit.demo.grpc.AnalyzeBookRequest,
              edu.rutmiit.demo.grpc.BookAnalysisResponse>(
                service, METHODID_ANALYZE_BOOK)))
        .build();
  }

  private static abstract class BookAnalyticsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BookAnalyticsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.rutmiit.demo.grpc.BookAnalyticsOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BookAnalytics");
    }
  }

  private static final class BookAnalyticsFileDescriptorSupplier
      extends BookAnalyticsBaseDescriptorSupplier {
    BookAnalyticsFileDescriptorSupplier() {}
  }

  private static final class BookAnalyticsMethodDescriptorSupplier
      extends BookAnalyticsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BookAnalyticsMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BookAnalyticsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BookAnalyticsFileDescriptorSupplier())
              .addMethod(getAnalyzeBookMethod())
              .build();
        }
      }
    }
    return result;
  }
}
