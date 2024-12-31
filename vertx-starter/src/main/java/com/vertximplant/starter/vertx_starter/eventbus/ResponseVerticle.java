package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseVerticle extends AbstractVerticle {

  public static final String MY_REQUEST_ADDRESS = "my.request.address";
  private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.eventBus().<String>consumer(MY_REQUEST_ADDRESS, message -> {
      LOG.debug("Recieved Message: {}", message.body());
      message.reply("Recieved Your message. Thanks!", new DeliveryOptions().setSendTimeout(30000));
    });
  }
}
