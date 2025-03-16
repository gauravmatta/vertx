package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.api.WatchListRestAPI;
import com.vertximplant.starter.vertx_big_board.helper.DBResponseHelper;
import com.vertximplant.starter.vertx_big_board.pojo.WatchList;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WatchListHandler implements Handler<RoutingContext> {

  Logger LOG = LoggerFactory.getLogger(WatchListHandler.class);
  final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<>();

  @Inject
  private DBResponseHelper dbResponseHelper;

  public void handle(RoutingContext routingContext) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = WatchListRestAPI.getAccountId(routingContext);
    Optional<WatchList> watchList =
        Optional.ofNullable(watchListPerAccount.get(UUID.fromString(accountId)));
    if (watchList.isEmpty()) {
      String response =
          new JsonObject().put("message", "Watchlist for account " + accountId + " not available")
              .put("path", routingContext.normalizedPath()).toString();
      dbResponseHelper.handleErrorResponse("t_test", routingContext, "QuotesId",
          new Failure(204, response), "Fetch Quotes", stopwatch);
      return;
    }
    final JsonObject response = watchList.get().toJsonObject();
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    dbResponseHelper.handleSuccessResponse("t_test", routingContext, watchList.get(), stopwatch);
  }

  public void handlePut(RoutingContext routingContext) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = WatchListRestAPI.getAccountId(routingContext);
    JsonObject json = routingContext.body().asJsonObject();
    WatchList watchList = json.mapTo(WatchList.class);
    watchListPerAccount.put(UUID.fromString(accountId), watchList);
    dbResponseHelper.handleSuccessResponse("t_test", routingContext, watchList, stopwatch);
  }

  public void handleDelete(RoutingContext routingContext) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = WatchListRestAPI.getAccountId(routingContext);
    final WatchList deleted = watchListPerAccount.remove(UUID.fromString(accountId));
    LOG.info("Deleted: {},Remaining: {}", deleted, watchListPerAccount.values());
    dbResponseHelper.handleSuccessResponse("t_test", routingContext, deleted, stopwatch);
  }
}
