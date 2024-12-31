package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseJsonVerticle extends AbstractVerticle {

  public static final String MY_REQUEST_ADDRESS = "my.request.address";
  private static final Logger LOG = LoggerFactory.getLogger(ResponseJsonVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.eventBus().<JsonObject>consumer(MY_REQUEST_ADDRESS, message -> {
      LOG.debug("Recieved Message: {}", message.body());
      message.reply(new JsonArray().add("one").add("two").add("three"),
          new DeliveryOptions().setSendTimeout(30000));
    });
  }
}
