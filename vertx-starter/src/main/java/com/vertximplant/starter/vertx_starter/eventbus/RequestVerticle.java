package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestVerticle extends AbstractVerticle {

  private final static Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);
  public static final String MY_REQUEST_ADDRESS = "my.request.address";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    EventBus eventBus = vertx.eventBus();
    String message = "Hello World";
    LOG.debug("Sending: {}",message);
    eventBus.<String>request(MY_REQUEST_ADDRESS, message, reply ->{
      LOG.debug("Response: {}",reply.result().body());
    });
  }
}
