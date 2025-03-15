package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.api.WatchListRestAPI;
import com.vertximplant.starter.vertx_big_board.helper.DBResponseHelper;
import com.vertximplant.starter.vertx_big_board.pojo.WatchList;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.SqlResult;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class PutWatchListDatabaseHandler {

  Logger LOG = LoggerFactory.getLogger(PutWatchListDatabaseHandler.class);
  @Inject
  private DBResponseHelper dbResponseHelper;

  public void handle(RoutingContext routingContext, Pool db) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    String accountId = WatchListRestAPI.getAccountId(routingContext);
    JsonObject jsonObject = routingContext.body().asJsonObject();
    WatchList watchList = jsonObject.mapTo(WatchList.class);
    // watchList.getAssets().forEach(asset -> {
    List<Map<String, Object>> parameterBatch = watchList.getAssets().stream().map(asset -> {
      final Map<String, Object> parameters = new HashMap<>();
      parameters.put("account_id", accountId);
      parameters.put("asset", asset.getName());
      return parameters;
    }).toList();

    db.withTransaction(client -> SqlTemplate
        .forUpdate(client, "Delete from broker.watchlist w where w.account_id = #{account_id}")
        .execute(Collections.singletonMap("account_id", accountId))
        .onFailure(DBResponseHelper.errorHandler(routingContext,
            "Failed to clear watchlist for accountId: " + accountId))
        .compose(
            deletionDone -> addAllForAccountId(client, routingContext, parameterBatch, accountId))
        .onFailure(DBResponseHelper.errorHandler(routingContext,
            "Failed to update watchlist for accountId: " + accountId))
        .onSuccess(result -> dbResponseHelper.handleEmptyResponse(UUID.randomUUID().toString(),
            routingContext, "WatchListId" + accountId, "Insert into WatchList", stopwatch)));
  }

  private Future<SqlResult<Void>> addAllForAccountId(final SqlConnection client,
      final RoutingContext routingContext, final List<Map<String, Object>> parameterBatch,
      String accountId) {
    return SqlTemplate
        .forUpdate(client,
            "INSERT INTO broker.watchlist (account_id, asset) VALUES(#{account_id},#{asset})"
                + " ON CONFLICT (account_id,asset) DO NOTHING")
        .executeBatch(parameterBatch).onFailure(DBResponseHelper.errorHandler(routingContext,
            "Failed to insert watchlist for accountId: " + accountId));
  }
}
