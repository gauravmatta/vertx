package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.api.WatchListRestAPI;
import com.vertximplant.starter.vertx_big_board.helper.DBResponseHelper;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class GetWatchListFromDatabaseHandler {

  Logger LOG = LoggerFactory.getLogger(GetWatchListFromDatabaseHandler.class);
  @Inject
  private DBResponseHelper dbResponseHelper;

  public void handle(RoutingContext routingContext, Pool db) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = WatchListRestAPI.getAccountId(routingContext);
    SqlTemplate
        .forQuery(db, "SELECT w.asset from broker.watchlist w where w.account_id=#{account_id}")
        .mapTo(Row::toJson).execute(Collections.singletonMap("account_id", accountId))
        .onFailure(DBResponseHelper.errorHandler(routingContext,
            "Failed to fetch watchlist for accountId: " + accountId))
        .onSuccess(assets -> {
          if (!assets.iterator().hasNext()) {
            String response = new JsonObject()
                .put("message", "watchlist for accountId " + accountId + " not found")
                .put("path", routingContext.normalizedPath()).toString();
            dbResponseHelper.handleEmptyResponse("t_test", routingContext, "WatchListId",
                new Failure(204, response), "Fetch WatchList", stopwatch);
            return;
          }
          JsonArray response = new JsonArray();
          assets.forEach(response::add);
          LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
          dbResponseHelper.handleSuccessJsonArrayResponse("t_test", routingContext, response,
              stopwatch);
        });
  }

}
