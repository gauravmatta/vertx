package com.vertximplant.starter.vertx_starter.eventloops;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopExample extends AbstractVerticle {

  public static final Logger LOG = LoggerFactory.getLogger(EventLoopExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(
      new VertxOptions()
        .setMaxEventLoopExecuteTime(500)
        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
        .setBlockedThreadCheckInterval(1)
        .setBlockedThreadCheckIntervalUnit(TimeUnit.MILLISECONDS)
    );
    vertx.deployVerticle(new EventLoopExample());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
   LOG.debug("Start {}",getClass().getName());
   startPromise.complete();
   //Never Block the event Loop
   Thread.sleep(5000);
  }
}
