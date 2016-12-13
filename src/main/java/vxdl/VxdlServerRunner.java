package vxdl;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class VxdlServerRunner implements ApplicationContextAware {

    @Autowired
    Vertx vertx;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void initialize() {
        int verticleCount = 4;
        for (int i = 1; i <= verticleCount; i++) {
            vertx.deployVerticle(new VxdlServerVerticle(applicationContext));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
