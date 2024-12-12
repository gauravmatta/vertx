package com.vertximplant.starter.vertx_starter.eventbus.customcoding;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PongVerticle extends AbstractVerticle {

  public static final String MY_REQUEST_ADDRESS = PingVerticle.class.getName();
  private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    EventBus eventBus = vertx.eventBus();
    eventBus.registerDefaultCodec(Pong.class,new LocalMessageCodec<>(Pong.class));
    vertx.eventBus().<Ping>consumer(MY_REQUEST_ADDRESS,message -> {
      LOG.debug("Recieved Message: {}",message.body());
      message.reply(new Pong(0),new DeliveryOptions().setSendTimeout(30000));
    }).exceptionHandler(error -> LOG.error("Error: ",error));
    startPromise.complete();
  }
}
