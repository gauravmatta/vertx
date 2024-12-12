package com.vertximplant.starter.vertx_starter.eventbus.customcoding;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingVerticle extends AbstractVerticle {

  private final static Logger LOG = LoggerFactory.getLogger(PingVerticle.class);
  public static final String MY_REQUEST_ADDRESS = PingVerticle.class.getName();

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    EventBus eventBus = vertx.eventBus();
    eventBus.registerDefaultCodec(Ping.class,new LocalMessageCodec<>(Ping.class));
    final Ping message = new Ping("Hello",true);
    LOG.debug("Sending: {}",message);
    eventBus.<Pong>request(MY_REQUEST_ADDRESS, message, reply ->{
      if(reply.failed()){
        LOG.error("Failed: ",reply.cause());
        return;
      }
      LOG.debug("Response: {}",reply.result().body());
    });
    startPromise.complete();
  }
}
