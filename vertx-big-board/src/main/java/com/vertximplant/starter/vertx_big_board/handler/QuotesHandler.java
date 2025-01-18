package com.vertximplant.starter.vertx_big_board.handler;

import com.vertximplant.starter.vertx_big_board.pojo.Asset;
import com.vertximplant.starter.vertx_big_board.pojo.Quote;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesHandler {
  Logger LOG = LoggerFactory.getLogger(QuotesHandler.class);
  public static final List<String> ASSETS = Arrays.asList("AADHARHFC", "ACC", "AFCONS", "ARE&M",
      "ASIANPAINT", "BAJAJ-AUTO", "BANKINDIA", "BEL", "BPCL");
  final Map<String, Quote> cachedQuotes = new HashMap<>();

  public void handle(RoutingContext routingContext) {
    ASSETS.forEach(symbol -> {
      cachedQuotes.put(symbol, initRandomQuote(symbol));
    });
    String assetParam = routingContext.pathParam("asset");
    LOG.debug("Asset parameter: {}", assetParam);
    Quote quote = cachedQuotes.get(assetParam);
    final JsonObject response = quote.toJsonObject();
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    routingContext.response().end(response.toBuffer());

  }

  private Quote initRandomQuote(String asset) {
    return Quote.builder().asset(new Asset(asset)).volume(randomValue()).ask(randomValue())
        .bid(randomValue()).lastPrice(randomValue()).build();
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
