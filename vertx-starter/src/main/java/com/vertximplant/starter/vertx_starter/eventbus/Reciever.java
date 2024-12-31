package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reciever extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(Reciever.class.getName());

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.eventBus().<String>consumer(Sender.class.getName(),
        message -> LOG.debug("Recieved: {}", message.body()));
  }
}
