package com.vertximplant.starter.vertx_big_board.verticles;

import com.vertximplant.starter.vertx_big_board.config.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionInfoVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VersionInfoVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.loadBrokerConfig(vertx).onFailure(startPromise::fail).onSuccess(brokerConfig -> {
      LOG.info("Current Application Version is: {}", brokerConfig.getVersion());
      startPromise.complete();
    });
  }
}
