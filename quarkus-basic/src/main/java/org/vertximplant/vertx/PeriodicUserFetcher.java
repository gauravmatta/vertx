package org.vertximplant.vertx;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PeriodicUserFetcher extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PeriodicUserFetcher.class);

    @Override
    public Uni<Void> asyncStart() {
//        vertx.periodicStream(Duration.ofSeconds(5).toMillis())
//                .toMulti()
//                .subscribe()
//                .with(item ->{
//                    LOG.info("Hello from periodic user fetcher");
//                });
        vertx.setPeriodic(Duration.ofSeconds(5).toMillis(),item->{
            LOG.info("Hello from periodic user fetcher");
        });
        return Uni.createFrom().voidItem();
    }


}
