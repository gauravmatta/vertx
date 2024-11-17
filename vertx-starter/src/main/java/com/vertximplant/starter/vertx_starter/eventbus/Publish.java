package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Publish extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(Publish.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    LOG.debug("Publishing Message");
    vertx.setPeriodic(Duration.ofSeconds(10).toMillis(),id -> vertx.eventBus().publish(Publish.class.getName(),"A message for everyone"));
  }
}
