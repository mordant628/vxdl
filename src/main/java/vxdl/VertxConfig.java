package vxdl;

import com.hazelcast.config.*;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
//import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
//import io.vertx.spi.cluster.impl.zookeeper.ZookeeperClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Andrew.Burtnett on 9/2/15.
 * <p/>
 * This Config Class will join the vert.x cluster and create a bean to be used in elsewhere.
 */
@Configuration
@ComponentScan(basePackages = "vxdl")
public class VertxConfig {
    private static Logger logger = LoggerFactory.getLogger(VertxConfig.class);


    //There seem to be scoping issues with the lambda so this needs to be at the Class level
    private Vertx vertx;

    @Bean
    public Vertx vertx() throws InterruptedException {
        //Setup system properties
        System.setProperty("vertx.cacheDirBase", ".vertx");

        //members and Interfaces
        String networkMembers = "localhost:2181";

        /*
        ClusterManager clusterManager = null;
        Properties zkConfig = new Properties();
        zkConfig.setProperty("hosts.zookeeper", networkMembers);
        zkConfig.setProperty("path.root", "vxdl");
        zkConfig.setProperty("retry.initialSleepTime", "100");
        zkConfig.setProperty("retry.intervalTimes", "10");
        zkConfig.setProperty("retry.maxTimes", "15");
        clusterManager = new ZookeeperClusterManager(zkConfig);
        */


        VertxOptions options = new VertxOptions(); //.setClusterManager(clusterManager);
        options.setClusterHost("localhost");
        options.setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));
        options.setBlockedThreadCheckInterval(1000 * 60 * 60);
        options.setWarningExceptionTime(50*1000);// wanna see whether eventloop is blocked longer than .05 ms

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                vertx = res.result();
            } else {
                throw new RuntimeException("Unable to launch Vert.x");
            }
        });

        int timeout = 60000;
        int count = 0;
        while (vertx == null) {
            logger.info("Sleeping for 1000 ms because vertx has yet to start.");
            Thread.sleep(1000);
            count += 1000;
            if (count > timeout) {
                logger.warn("Uh oh.  Something isn't quite right.  Vertx has taken longer than it should have.  " +
                            "Time taken: {} ms  VertxStartupTimeout: {} ms", count, timeout);
            }
        }

        logger.info("Vertx started and took roughly {} ms", count);

        return vertx;
    }
}
