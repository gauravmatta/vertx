package com.vertximplant.starter.vertx_big_board.handler;

import com.google.inject.Singleton;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AssetsHandler {

  private static final Logger LOG = LoggerFactory.getLogger(AssetsHandler.class);

  public void handle(RoutingContext routingContext) {
    final JsonArray response = new JsonArray();
    response
      .add(new JsonObject().put("symbol", "AAPL"))
      .add(new JsonObject().put("symbol", "AMZN"))
      .add(new JsonObject().put("symbol", "NFLX"))
      .add(new JsonObject().put("symbol", "TSLA"));
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    routingContext.response().end(response.toBuffer());
  }

}
