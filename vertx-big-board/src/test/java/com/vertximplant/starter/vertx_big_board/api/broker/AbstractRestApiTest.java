package com.vertximplant.starter.vertx_big_board.api.broker;

import com.vertximplant.starter.vertx_big_board.config.ConfigLoader;
import com.vertximplant.starter.vertx_big_board.verticles.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRestApiTest {
  protected static final int TEST_SERVER_PORT = 9000;
  private static final Logger LOG = LoggerFactory.getLogger(AbstractRestApiTest.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    System.setProperty(ConfigLoader.SERVER_PORT, String.valueOf(TEST_SERVER_PORT));
    System.setProperty(ConfigLoader.DB_HOST, "localhost");
    System.setProperty(ConfigLoader.DB_PORT, "5432");
    System.setProperty(ConfigLoader.DB_DATABASE, "vertx-big-board");
    System.setProperty(ConfigLoader.DB_USER, "postgres");
    System.setProperty(ConfigLoader.DB_PASSWORD, "postgres");
    LOG.warn("Tests are using local database instance !!!");
    vertx.deployVerticle(new MainVerticle())
        .onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }
}
