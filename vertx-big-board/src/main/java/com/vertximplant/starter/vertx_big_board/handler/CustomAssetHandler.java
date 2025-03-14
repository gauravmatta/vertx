package com.vertximplant.starter.vertx_big_board.handler;

import com.vertximplant.starter.vertx_big_board.pojo.Asset;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

public class CustomAssetHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(CustomAssetHandler.class);
  public static final List<String> ASSETS = Arrays.asList("AADHARHFC", "ACC", "AFCONS", "ARE&M",
      "ASIANPAINT", "BAJAJ-AUTO", "BANKINDIA", "BEL", "BPCL");

  @Override
  public void handle(RoutingContext routingContext) {
    final JsonArray response = new JsonArray();
    ASSETS.stream().map(Asset::new).forEach(response::add);
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    routingContext.response().end(response.toBuffer());
  }
}
