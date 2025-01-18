package com.vertximplant.starter.vertx_big_board.handler;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.pojo.Asset;
import com.vertximplant.starter.vertx_big_board.pojo.Quote;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import com.vertximplant.starter.vertx_big_board.util.AppResponseBuilder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static com.vertximplant.starter.vertx_big_board.helper.GSONHelper.gsonToString;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;

public class QuotesHandler {

  @Inject
  private AppResponseBuilder responseBuilder;

  Logger LOG = LoggerFactory.getLogger(QuotesHandler.class);

  public static final List<String> ASSETS = Arrays.asList("AADHARHFC", "ACC", "AFCONS", "ARE&M",
      "ASIANPAINT", "BAJAJ-AUTO", "BANKINDIA", "BEL", "BPCL");

  final Map<String, Quote> cachedQuotes = new HashMap<>();

  public void handle(RoutingContext routingContext) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    ASSETS.forEach(symbol -> {
      cachedQuotes.put(symbol, initRandomQuote(symbol));
    });
    String assetParam = routingContext.pathParam("asset");
    LOG.debug("Asset parameter: {}", assetParam);
    Optional<Quote> quote = Optional.ofNullable(cachedQuotes.get(assetParam));
    if (quote.isEmpty()) {
      String response =
          new JsonObject().put("message", "quote for asset " + assetParam + " not found")
              .put("path", routingContext.normalizedPath()).toString();
      handleErrorResponse("t_test", routingContext, "userId", new Failure(500, response),
          stopwatch);
      return;
    }
    final JsonObject response = quote.get().toJsonObject();
    LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode());
    routingContext.response().end(response.toBuffer());

  }

  private void handleSuccessResponse(String transid, RoutingContext routingContext, Quote quote,
      Stopwatch stopwatch) {
    responseBuilder.sendResponse(routingContext.request(), SC_OK, gsonToString(quote),
        responseBuilder.buildResponseHeaders(transid), stopwatch);
  }

  private void handleErrorResponse(String transid, RoutingContext routingContext, String identifier,
      Throwable throwable, Stopwatch stopwatch) {
    responseBuilder.exceptionResponseHandler(transid,
        responseBuilder.buildLogEndIdentifier("user_id", identifier), "Get Quotes Information",
        throwable, routingContext.request(), ((Failure) throwable).getStatusMsg(), stopwatch);
  }

  private Quote initRandomQuote(String asset) {
    return Quote.builder().asset(new Asset(asset)).volume(randomValue()).ask(randomValue())
        .bid(randomValue()).lastPrice(randomValue()).build();
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
