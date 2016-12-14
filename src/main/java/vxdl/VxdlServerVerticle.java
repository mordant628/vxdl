package vxdl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

//import promethean.collab.handler.CollabBridgeEventImpl;
//import promethean.collab.handler.CollabFormHandler;

import vxdl.handler.VxdlSockJSHandler;
import vxdl.security.VxdlAuthProvider;

import static org.slf4j.LoggerFactory.getLogger;

public class VxdlServerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = getLogger(VxdlServerVerticle.class);

    private static final String SOCKJS_MIN_JS = "sockjs.js";

    private ApplicationContext context;

    public VxdlServerVerticle() {
    }

    public VxdlServerVerticle(ApplicationContext applicationContext) {
        context = applicationContext;

    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route().handler(CookieHandler.create());

        router.route()
            .handler(SessionHandler.create(LocalSessionStore.create(vertx))
                     .setNagHttps(false)
                     .setSessionTimeout(60 * 60 * 1000));  // 60 minute timeout to match AF

        AuthProvider authProvider = new VxdlAuthProvider();

        // We need a user session handler too to make sure the user is stored in the session between requests
        router.route().handler(UserSessionHandler.create(authProvider));

        int pingTimeout = 10000;
        BridgeOptions bridgeOptions = new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddressRegex(".*"))
                .setPingTimeout(pingTimeout)
                .addOutboundPermitted(new PermittedOptions().setAddressRegex(".*"));


        SockJSHandlerOptions sockJSHandlerOptions = new SockJSHandlerOptions();
        sockJSHandlerOptions.setLibraryURL("http://localhost:9080/"+SOCKJS_MIN_JS);

        VxdlSockJSHandler ebHandler = new VxdlSockJSHandler(vertx, sockJSHandlerOptions)
            .bridge(bridgeOptions, new Handler<BridgeEvent>() {
                    @Override
                    public void handle(BridgeEvent event) {
                        event.complete(true);
                    }
                });

        router.route("/eventbus/*").handler(ebHandler);
        router.route("/vertxstats/*").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                MetricsService metricsService = MetricsService.create(vertx);
                JsonObject metrics = metricsService.getMetricsSnapshot(vertx);
                String out = metrics.toString();
                LOGGER.info(" STATS {} ", out);
                routingContext.response().end(out);
            }
        });

        router.route().handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                HttpServerResponse response = routingContext.response();
                response.putHeader("content-type", "text/plain");
                routingContext.response().end("hello world from vxdl:  Vertx DeadLock");
            }
        });

        // Start the web server and tell it to use the router to handle requests.
        HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(131070);
        HttpServer httpServer = vertx
            .createHttpServer(options)
            .requestHandler(router::accept)
            .listen(9080);

    }

}
