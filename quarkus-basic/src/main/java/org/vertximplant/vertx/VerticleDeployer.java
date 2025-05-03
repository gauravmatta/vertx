package org.vertximplant.vertx;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.enterprise.inject.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class VerticleDeployer {

    private static final Logger LOG = LoggerFactory.getLogger(VerticleDeployer.class);

    public void init(@Observes StartupEvent startupEvent, Vertx vertx, Instance<AbstractVerticle> verticals){
        LOG.info("Deploying Verticles");
        verticals.forEach(v->
            vertx.deployVerticle(v).await().indefinitely()
        );
    }
}
