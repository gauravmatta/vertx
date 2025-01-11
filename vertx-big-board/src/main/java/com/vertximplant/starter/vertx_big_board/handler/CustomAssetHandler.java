package com.vertximplant.starter.vertx_big_board.handler;

import com.vertximplant.starter.vertx_big_board.pojo.Asset;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomAssetHandler {
  private static final Logger LOG = LoggerFactory.getLogger(CustomAssetHandler.class);

  public void handle(RoutingContext routingContext) {
    final JsonArray response = new JsonArray();
    response.add(new Asset("AADHARHFC")).add(new Asset("ACC")).add(new Asset("AFCONS"))
        .add(new Asset("ARE&M")).add(new Asset("ASIANPAINT")).add(new Asset("BAJAJ-AUTO"))
        .add(new Asset("BANKINDIA")).add(new Asset("BEL")).add(new Asset("BPCL"));
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    routingContext.response().end(response.toBuffer());
  }
}
