package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscriber extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(Subscriber.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.eventBus().<String>consumer(Publish.class.getName(),message -> LOG.debug("Received by Subscriber : {}", message.body()));
  }
}
