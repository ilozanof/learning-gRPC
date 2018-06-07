package com.ilozanof.learning.gRPC.routeGuide;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * A sample gRPC server that serve the RouteGuide (see route_guide.proto) service.
 */
public class RouteGuideServer {
    private static final Logger logger = Logger.getLogger(RouteGuideServer.class.getName());

    private final int port;
    private final Server server;

    public RouteGuideServer(int port) throws IOException {
        this(port, RouteGuideUtil.getDefaultFeaturesFile());
    }

    /**
     * Create a RouteGuide server listening on {@code port} using {@code featureFile} database.
     */
    public RouteGuideServer(int port, URL featureFile) throws IOException {
        this(ServerBuilder.forPort(port), port, RouteGuideUtil.parseFeatures(featureFile));
    }

    /**
     * Create a RouteGuide server using serverBuilder as a base and features as data.
     */
    public RouteGuideServer(ServerBuilder<?> serverBuilder, int port, Collection<Feature> features) {
        this.port = port;
        server = serverBuilder.addService(new RouteGuideService(features))
                .build();
    }

    /**
     * Start serving requests.
     */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                RouteGuideServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /**
     * Stop serving requests and shutdown resources.
     */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main method.  This comment makes the linter happy.
     */
    public static void main(String[] args) throws Exception {
        RouteGuideServer server = new RouteGuideServer(8980);
        server.start();
        server.blockUntilShutdown();
    }
}