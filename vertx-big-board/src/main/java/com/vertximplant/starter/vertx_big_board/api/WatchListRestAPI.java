package com.vertximplant.starter.vertx_big_board.api;

import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.handler.DeleteWatchListDatabaseHandler;
import com.vertximplant.starter.vertx_big_board.handler.GetWatchListFromDatabaseHandler;
import com.vertximplant.starter.vertx_big_board.handler.PutWatchListDatabaseHandler;
import com.vertximplant.starter.vertx_big_board.handler.WatchListHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchListRestAPI {
  private static final Logger LOG = LoggerFactory.getLogger(WatchListRestAPI.class);

  @Inject
  private WatchListHandler watchListHandler;

  @Inject
  private GetWatchListFromDatabaseHandler getWatchListFromDatabaseHandler;

  @Inject
  private PutWatchListDatabaseHandler putWatchListDatabaseHandler;

  @Inject
  private DeleteWatchListDatabaseHandler deleteWatchListDatabaseHandler;

  public void attach(Router router, Pool db) {
    final String path = "/account/watchlist/:accountId";
    router.get(path).handler(watchListHandler);
    router.put(path).handler(watchListHandler::handlePut);
    router.delete(path).handler(watchListHandler::handleDelete);
    String pgPath = "/pg/account/watchlist/:accountId";
    router.get(pgPath)
        .handler(routingContext -> getWatchListFromDatabaseHandler.handle(routingContext, db));
    router.put(pgPath)
        .handler(routingContext -> putWatchListDatabaseHandler.handle(routingContext, db));
    router.delete(pgPath)
        .handler(routingContext -> deleteWatchListDatabaseHandler.handle(routingContext, db));
  }

  public static String getAccountId(RoutingContext routingContext) {
    String accountId = routingContext.pathParam("accountId");
    LOG.debug("{} for account {}", routingContext.normalizedPath(), accountId);
    return accountId;
  }
}
