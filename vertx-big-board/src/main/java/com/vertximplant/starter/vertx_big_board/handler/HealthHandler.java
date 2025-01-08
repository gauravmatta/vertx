package com.vertximplant.starter.vertx_big_board.handler;

import com.google.inject.Singleton;
import com.vertximplant.starter.vertx_big_board.constants.HttpConstants;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HealthHandler {

  private static final Logger LOG = LoggerFactory.getLogger(HealthHandler.class);

  public void handle(RoutingContext routingContext) {
    routingContext.response().setStatusCode(HttpConstants.HTTP_STATUS_200)
      .putHeader(HttpConstants.HTTP_HEADER_CONTENT_TYPE,
        HttpConstants.HTTP_HEADER_CONTENT_VALUE + "; charset=utf-8")
      .end(Json.encodePrettily("Health OK"));
  }
}
