package com.vertximplant.starter.vertx_big_board.handler;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAssetsFromDatabaseHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(GetAssetsFromDatabaseHandler.class);
  private final PgPool db;

  public GetAssetsFromDatabaseHandler(PgPool db) {
    this.db = db;
  }

  @Override
  public void handle(RoutingContext routingContext) {

    db.query("SELECT a.value FROM broker.assets a").execute().onFailure(error -> {
      LOG.error("DB Failure: ", error);
      routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(new JsonObject().put("message", "Failed to get assets from db!")
              .put("path", routingContext.normalizedPath()).toBuffer());
    }).onSuccess(result -> {
      JsonArray response = new JsonArray();
      result.forEach(row -> {
        response.add(row.getValue("value"));
      });
      LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
      routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(response.toBuffer());
    });
  }
}
