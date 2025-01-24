package com.vertximplant.starter.vertx_big_board.api;

import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.handler.WatchListHandler;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchListRestAPI {
  private static final Logger LOG = LoggerFactory.getLogger(WatchListRestAPI.class);



  @Inject
  private WatchListHandler watchListHandler;

  public void attach(Router router) {
    final String path = "/account/watchlist/:accountId";
    router.get(path).handler(watchListHandler);
    router.put(path).handler(watchListHandler::handlePut);
    router.delete(path).handler(watchListHandler::handleDelete);
  }
}
