package vxdl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
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

        // TODO:  Need to restrict the origin???
        /*
        router.route("/loginHandler").handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.GET)
                .allowedHeader("content-type")
                .allowedHeader("X-Requested-With")
                .allowedHeader("accept")
                .allowCredentials(true)
                .maxAgeSeconds(3000));
        */
        // We need cookies and sessions
        router.route().handler(CookieHandler.create());

        /*
        // only needed for login.  It will cause problems with the rest of the eventbus
        router.route("/loginHandler").handler(BodyHandler.create());
        */
        router.route()
            .handler(SessionHandler.create(LocalSessionStore.create(vertx))
                     .setNagHttps(false)
                     .setSessionTimeout(60 * 60 * 1000));  // 60 minute timeout to match AF

        AuthProvider authProvider = new VxdlAuthProvider();

        // We need a user session handler too to make sure the user is stored in the session between requests
        router.route().handler(UserSessionHandler.create(authProvider));

        // Handles the actual login
        /*
        router.route("/loginHandler").handler(CollabFormHandler.create(authProvider));

        router.route("/isLoggedIn").handler(context -> {
            Boolean loggedInStatus = Boolean.FALSE;
            if (context.user() != null) {
                loggedInStatus = Boolean.TRUE;
            }
            context.response().putHeader("content-type", "text/plain").setStatusCode(200).end(loggedInStatus.toString());
        });

        // Implement logout
        router.route("/logout").handler(context -> {
            context.clearUser();
            context.response().putHeader("content-type", "text/plain").setStatusCode(200).end("Logout success");
        });
        */

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
                        boolean successStatus = true;
                        event.complete(successStatus);
                        /*
                        if (event instanceof CollabBridgeEventImpl) {
                            CollabBridgeEventImpl bridgeEvent = (CollabBridgeEventImpl) event;


                            // this sets the tenant on the thread for all to use.  especially useful when using cache objects
                            setTenantContextHolder(event);

                            LOGGER.trace(bridgeEvent.collabType().name());
                            switch (bridgeEvent.collabType()) {
                                case REGISTER:
                                    // this code will be removed before going to prod. This is only to fix the limitation of the test client
                                    if (CollabUtils.LOADTEST2.equals(collabProperties.getEnviromentName())) {
                                        setupCollabAuthToken(event);
                                    }
                                    successStatus = collabManager.handleRegister(event);
                                    break;
                                case UNREGISTER:
                                    collabManager.handleUnRegister(event);
                                    break;
                                case RECEIVE:
                                    collabManager.handleReceive(event);
                                    break;
                                case PUBLISH:
                                    successStatus = collabManager.handlePublish(event);
                                    break;
                                case SOCKET_CREATED:
                                    collabManager.handleSocketCreated(event);
                                    break;
                                case SOCKET_CLOSED:
                                    collabManager.handleSocketClosed(event);
                                    break;
                                case SEND:
                                    collabManager.handleSend(event);
                                    break;
                                case PING:
                                    collabManager.refreshSocketCaches(event);
                                    break;
                                default:
                                    LOGGER.info("Not handling event type yet:  " + bridgeEvent.collabType().name());
                            }

                            event.complete(successStatus);
                        } else {
                            LOGGER.error("BridgeEvent was sent that was not an instance of CollabBridgeEventImpl.");
                        }
                        */
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

        // Create a router endpoint for the static content.
        // This defaults to webroot subdir
        router.route().handler(StaticHandler.create());

        // Start the web server and tell it to use the router to handle requests.
        HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(131070);
        HttpServer httpServer = vertx
            .createHttpServer(options)
            .requestHandler(router::accept)
            .listen(9080);

    }


    private void setupCollabAuthToken(BridgeEvent event) {
        /*
        JsonObject msg = event.rawMessage();
        if (msg.containsKey("COLLABTOKEN")) {
            Session session = event.socket().webSession();
            if (session.get("COLLABTOKEN") == null) {

                session.put("COLLABTOKEN", msg.getString("COLLABTOKEN"));
                collabManager.updateSocketCreated(event);
            }

        }
        */
    }

}
