package vxdl.security;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * User:  Shane Muffat
 * Date:  10/1/15
 * Time:  1:46 PM
 */
public class VxdlUser extends AbstractUser {

    private String collabToken;
    private AuthProvider authProvider;
    private JsonObject principal;

    private VxdlUser() {
    }

    public VxdlUser(String collabToken, AuthProvider provider) {
        this.collabToken = collabToken;
        this.authProvider = provider;

        principal = new JsonObject().put("collabToken", collabToken);
    }

    @Override
    protected void doIsPermitted(String permissionOrRole, Handler<AsyncResult<Boolean>> handler) {
        // allow all permissions
        handler.handle(Future.succeededFuture(true));
    }

    @Override
    public JsonObject principal() {
        return principal;
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }
}
