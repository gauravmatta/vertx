package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.helper.DBResponseHelper;
import com.vertximplant.starter.vertx_big_board.pojo.QuoteEntity;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class GetQuoteFromDatabaseHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GetQuoteFromDatabaseHandler.class);

  @Inject
  private DBResponseHelper dbResponseHelper;

  public void handle(RoutingContext routingContext, Pool db) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    final String assetParam = routingContext.pathParam("asset");
    LOG.debug("Asset parameter: {}", assetParam);
    SqlTemplate.forQuery(db,
        "SELECT q.asset,q.bid,q.ask,q.last_price,q.volume from broker.quotes q where asset =#{asset}")
        .mapTo(QuoteEntity.class).execute(Collections.singletonMap("asset", assetParam))
        .onFailure(DBResponseHelper.errorHandler(routingContext,
            "Failed to get quote for asset " + assetParam + " from db!"))
        .onSuccess(quotes -> {
          if (!quotes.iterator().hasNext()) {
            String response =
                new JsonObject().put("message", "quote for asset " + assetParam + " not found")
                    .put("path", routingContext.normalizedPath()).toString();
            dbResponseHelper.handleErrorResponse("t_test", routingContext, "QuotesId",
                new Failure(204, response), "Fetch Quotes", stopwatch);
            return;
          }
          JsonObject response = quotes.iterator().next().toJsonObject();
          LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
          dbResponseHelper.handleSuccessJsonObjectResponse("t_test", routingContext, response,
              stopwatch);
        });
  }


}
