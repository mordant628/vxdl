package vxdl;

import com.hazelcast.core.Hazelcast;
import java.util.concurrent.atomic.AtomicBoolean;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;


import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by mallik.peddagolla on 10/1/2015.
 */
@Component
public class VxdlLifecycle implements SmartLifecycle {
    private static final Logger LOGGER = getLogger(VxdlLifecycle.class);

    @Autowired
    Vertx vertx;

    private AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        synchronized(started){
            if(started.get()){
                stop();
                callback.run();
                LOGGER.info("The vxdl lifecycle has stopped.");
                started.set(false);
            }
        }
    }

    @Override
    public void start() {
        synchronized(started){
            if( ! started.get() ){
                started.set(true);
                LOGGER.info("The vxdl lifecycle has started.");
            }
        }
    }

    @Override
    public void stop() {
        LOGGER.info("### vxdl Shutdown initiated ### ");

        synchronized(started){
            if (started.get()) {
                started.set(false);
                if (vertx != null) {
                    LOGGER.info(" ******** closing vertx instance ************* ");
                    vertx.close(res -> {
                            if (res.succeeded()) {
                                LOGGER.info("****** Vertx closed successfully *********");
                                LOGGER.info("****** deployed verticles count {} after closing vertx *********", vertx.deploymentIDs().size());   // should always be ZERO
                            } else {
                                LOGGER.info("****** Unable to close Vertx **********");
                            }
                        });
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        return started.get();
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
