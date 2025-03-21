package com.vertximplant.starter.vertx_big_board.handler;

import com.google.inject.Singleton;
import com.vertximplant.starter.vertx_big_board.constants.HttpConstants;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

@Singleton
public class AssetsHandler implements Handler<RoutingContext> {

  private static final Logger LOG = LoggerFactory.getLogger(AssetsHandler.class);

  @Override
  public void handle(final RoutingContext routingContext) {
    final JsonArray response = new JsonArray();
    response.add(new JsonObject().put("symbol", "ICICIB22"))
        .add(new JsonObject().put("symbol", "CARRARO"))
        .add(new JsonObject().put("symbol", "COALINDIA"))
        .add(new JsonObject().put("symbol", "CPSEETF"))
        .add(new JsonObject().put("symbol", "EXIDEIND"));
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    artificialSleep(routingContext);
    routingContext.response()
        .putHeader(HttpConstants.HTTP_HEADER_CONTENT_TYPE, HttpConstants.HTTP_HEADER_CONTENT_VALUE)
        .putHeader("my-header", "my-value").end(response.toBuffer());
  }

  /**
   * Simulating a delay Used to simulate scaling and load testing
   *
   * @param routingContext routing context
   */
  private static void artificialSleep(RoutingContext routingContext) {
    try {
      // Simulating a delay
      final int random = ThreadLocalRandom.current().nextInt(100, 300);
      if (random % 2 == 0) {
        Thread.sleep(random);
        throw new RuntimeException("Simulating a failure...");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
