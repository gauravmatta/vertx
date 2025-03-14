package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.api.WatchListRestAPI;
import com.vertximplant.starter.vertx_big_board.helper.DBResponseHelper;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.UUID;

public class DeleteWatchListDatabaseHandler {

  Logger LOG = LoggerFactory.getLogger(DeleteWatchListDatabaseHandler.class);

  @Inject
  private DBResponseHelper dbResponseHelper;

  public void handle(RoutingContext routingContext, Pool db) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = WatchListRestAPI.getAccountId(routingContext);
    SqlTemplate.forUpdate(db, "DELETE FROM broker.watchlist where account_id=#{account_id}")
        .execute(Collections.singletonMap("account_id", accountId)).onFailure(DBResponseHelper
            .errorHandler(routingContext, "Failed to delete watchlist for accountId: " + accountId))
        .onSuccess(result -> {
          LOG.debug("Deleted {} rows for accountId {} ", result.rowCount(), accountId);
          dbResponseHelper.handleEmptyResponse(UUID.randomUUID().toString(), routingContext,
              "WatchListId" + accountId, "Delete From WatchList", stopwatch);
        });
  }
}
