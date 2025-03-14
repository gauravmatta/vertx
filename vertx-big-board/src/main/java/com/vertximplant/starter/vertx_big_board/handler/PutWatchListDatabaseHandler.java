package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.api.WatchListRestAPI;
import com.vertximplant.starter.vertx_big_board.helper.DBResponseHelper;
import com.vertximplant.starter.vertx_big_board.pojo.WatchList;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.vertximplant.starter.vertx_big_board.helper.GSONHelper.gsonToString;

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

    SqlTemplate
        .forUpdate(db,
            "INSERT INTO broker.watchlist (account_id, asset) VALUES(#{account_id},#{asset})"+" ON CONFLICT (account_id,asset) DO NOTHING")
        .executeBatch(parameterBatch).onFailure(DBResponseHelper.errorHandler(routingContext,
            "Failed to insert watchlist for accountId: " + accountId))
        .onSuccess(result -> {
           dbResponseHelper.handleEmptyResponse("t_test", routingContext, "WatchListId",
           new Failure(HttpResponseStatus.NO_CONTENT.code(), ""), "Insert in WatchList",
           stopwatch);
        });
  }
}
