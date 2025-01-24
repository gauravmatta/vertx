package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.pojo.Quote;
import com.vertximplant.starter.vertx_big_board.pojo.WatchList;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import com.vertximplant.starter.vertx_big_board.util.AppResponseBuilder;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import static com.vertximplant.starter.vertx_big_board.helper.GSONHelper.gsonToString;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;

public class WatchListHandler {

  Logger LOG = LoggerFactory.getLogger(WatchListHandler.class);
  final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();
  @Inject
  private AppResponseBuilder responseBuilder;

  public void handle(RoutingContext routingContext) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = getAccountId(routingContext);
    Optional<WatchList> watchList =
        Optional.ofNullable(watchListPerAccount.get(UUID.fromString(accountId)));
    if (watchList.isEmpty()) {
      String response =
          new JsonObject().put("message", "Watchlist for account " + accountId + " not available")
              .put("path", routingContext.normalizedPath()).toString();
      handleErrorResponse("t_test", routingContext, "userId", new Failure(404, response),
          stopwatch);
      return;
    }
    final JsonObject response = watchList.get().toJsonObject();
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    handleSuccessResponse("t_test", routingContext, watchList.get(), stopwatch);
  }

  public void handlePut(RoutingContext routingContext) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = getAccountId(routingContext);
    JsonObject json = routingContext.body().asJsonObject();
    WatchList watchList = json.mapTo(WatchList.class);
    watchListPerAccount.put(UUID.fromString(accountId), watchList);
    handleSuccessResponse("t_test", routingContext, watchList, stopwatch);
  }

  private String getAccountId(RoutingContext routingContext) {
    String accountId = routingContext.pathParam("accountId");
    LOG.debug("{} for account {}", routingContext.normalizedPath(), accountId);
    return accountId;
  }

  public void handleDelete(RoutingContext routingContext) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = getAccountId(routingContext);
    final WatchList deleted = watchListPerAccount.remove(UUID.fromString(accountId));
    LOG.info("Deleted: {},Remaining: {}",deleted,watchListPerAccount.values());
    handleSuccessResponse("t_test", routingContext, deleted, stopwatch);
  }

  private void handleSuccessResponse(String transid, RoutingContext routingContext,
      WatchList watchList, Stopwatch stopwatch) {
    responseBuilder.sendOnlyResponse(routingContext.request(), SC_OK, gsonToString(watchList),
        responseBuilder.buildResponseHeaders(transid), stopwatch);
  }

  private void handleErrorResponse(String transid, RoutingContext routingContext, String identifier,
      Throwable throwable, Stopwatch stopwatch) {
    responseBuilder.exceptionResponseHandler(transid,
        responseBuilder.buildLogEndIdentifier("user_id", identifier), "Get Quotes Information",
        throwable, routingContext.request(), ((Failure) throwable).getStatusMsg(), stopwatch);
  }
}
