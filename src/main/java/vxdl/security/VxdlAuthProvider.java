package vxdl.security;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import org.slf4j.Logger;
import org.springframework.util.StopWatch;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * User:  Shane Muffat
 * Date:  10/1/15
 * Time:  1:43 PM
 */
public class VxdlAuthProvider implements AuthProvider {
    private static final Logger LOGGER = getLogger(VxdlAuthProvider.class);

    public static final String COLLAB_TOKEN_CREDENTIAL_FIELD = "collabToken";

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
        String collabToken = authInfo.getString(COLLAB_TOKEN_CREDENTIAL_FIELD);

        if (collabToken == null) {
            handler.handle(Future.failedFuture("authInfo must contain 'collabToken' field"));
            return;
        }

        // let everyone authenticate
        handler.handle(Future.succeededFuture(new VxdlUser(collabToken, this)));
    }
}
