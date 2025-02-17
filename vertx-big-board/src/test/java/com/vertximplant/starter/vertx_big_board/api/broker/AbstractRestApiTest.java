package com.vertximplant.starter.vertx_big_board.api.broker;

import com.vertximplant.starter.vertx_big_board.config.ConfigLoader;
import com.vertximplant.starter.vertx_big_board.verticles.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractRestApiTest {
  protected static final int TEST_SERVER_PORT = 9000;

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    System.setProperty(ConfigLoader.SERVER_PORT, String.valueOf(TEST_SERVER_PORT));
    vertx.deployVerticle(new MainVerticle())
      .onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }
}
