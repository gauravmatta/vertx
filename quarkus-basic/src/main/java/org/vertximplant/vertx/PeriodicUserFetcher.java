package org.vertximplant.vertx;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@ApplicationScoped
public class PeriodicUserFetcher extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PeriodicUserFetcher.class);
    static final String ADDRESS = PeriodicUserFetcher.class.getName();

    @Override
    public Uni<Void> asyncStart() {
        WebClient client = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost("localhost").setDefaultPort(8080));
        vertx.setPeriodic(Duration.ofSeconds(5).toMillis(),item->{
            LOG.info("Fetch All Users");
            client.get("/users").send().subscribe().with(result -> {
                JsonArray body = result.bodyAsJsonArray();
                LOG.info("All users from http response : {}",body);
                vertx.eventBus().publish(ADDRESS,body);
            });
        });
        return Uni.createFrom().voidItem();
    }


}
