package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class Sender extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.setPeriodic(1000,
        id -> vertx.eventBus().send(Sender.class.getName(), "Sending a message..."));
  }
}
