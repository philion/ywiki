package com.acmerocket.ywiki;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class WikiMain {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WikiMain.class);

    public static void main(String[] args) {
        final String BASE_URI = "http://localhost:8080";

        final ResourceConfig rc = new ResourceConfig().register(WikiBinder.INSTANCE).packages("com.acmerocket.ywiki");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        LOG.info("Started server: {}", server);
        
        // cleanup?
        // This could be more robust...
    }
}
