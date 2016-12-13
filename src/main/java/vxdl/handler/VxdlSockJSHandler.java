package vxdl.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.handler.sockjs.impl.SockJSHandlerImpl;

/**
 * Created by Daniel Lynch on 12/16/15.
 * This is a wrapper for SockJSHandlerImpl so that we can use the custom EventBusBridgeImpl
 * https://github.com/vert-x3/vertx-web/blob/master/vertx-web/src/main/java/io/vertx/ext/web/handler/sockjs/impl/SockJSHandlerImpl.java
 */
public class VxdlSockJSHandler extends SockJSHandlerImpl {

    private Vertx vertx;

    public VxdlSockJSHandler(Vertx vertx, SockJSHandlerOptions options) {
        super(vertx, options);
        this.vertx = vertx;
    }

    public VxdlSockJSHandler bridge(BridgeOptions bridgeOptions,
                                     Handler<BridgeEvent> bridgeEventHandler) {
        //socketHandler(new VxdlEventBusBridgeImpl(vertx, bridgeOptions, bridgeEventHandler));
        return this;
    }
}
