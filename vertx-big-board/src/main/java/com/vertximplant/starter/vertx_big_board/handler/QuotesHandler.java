package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.helper.DBResponseHelper;
import com.vertximplant.starter.vertx_big_board.pojo.Asset;
import com.vertximplant.starter.vertx_big_board.pojo.Quote;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesHandler implements Handler<RoutingContext> {

  @Inject
  private DBResponseHelper dbResponseHelper;

  Logger LOG = LoggerFactory.getLogger(QuotesHandler.class);

  public static final List<String> ASSETS = Arrays.asList("AADHARHFC", "ACC", "AFCONS", "ARE&M",
      "ASIANPAINT", "BAJAJ-AUTO", "BANKINDIA", "BEL", "BPCL");

  final Map<String, Quote> cachedQuotes = new HashMap<>();

  @Override
  public void handle(RoutingContext routingContext) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    ASSETS.forEach(symbol -> cachedQuotes.put(symbol, initRandomQuote(symbol)));
    String assetParam = routingContext.pathParam("asset");
    LOG.debug("Asset parameter: {}", assetParam);
    Optional<Quote> quote = Optional.ofNullable(cachedQuotes.get(assetParam));
    if (quote.isEmpty()) {
      String response =
          new JsonObject().put("message", "quote for asset " + assetParam + " not found")
              .put("path", routingContext.normalizedPath()).toString();
      dbResponseHelper.handleEmptyResponse("t_test", routingContext, "QuotesId",
          new Failure(404, response), "Fetch Quotes", stopwatch);
      return;
    }
    final JsonObject response = quote.get().toJsonObject();
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    dbResponseHelper.handleSuccessResponse("t_test", routingContext, quote.get(), stopwatch);
  }

  private Quote initRandomQuote(String asset) {
    return Quote.builder().asset(new Asset(asset)).volume(randomValue()).ask(randomValue())
        .bid(randomValue()).lastPrice(randomValue()).build();
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
