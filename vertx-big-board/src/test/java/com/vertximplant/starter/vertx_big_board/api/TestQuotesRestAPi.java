package com.vertximplant.starter.vertx_big_board.api;

import com.vertximplant.starter.vertx_big_board.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.vertximplant.starter.vertx_big_board.constants.HttpConstants.PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestQuotesRestAPi {

  private static final Logger LOG = LoggerFactory.getLogger(TestQuotesRestAPi.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle())
        .onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void returns_quote_for_asset(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(PORT));
    client.get("/quotes/BANKINDIA").send().onComplete(testContext.succeeding(bufferHttpResponse -> {
      JsonObject json = bufferHttpResponse.bodyAsJsonObject();
      LOG.info("Response: {}", json);
      assertEquals("{\"name\":\"BANKINDIA\"}", json.getJsonObject("asset").encode());
      assertEquals(200, bufferHttpResponse.statusCode());
      testContext.completeNow();
    }));
  }

  @Test
  void returns_not_found_for_unknown_asset(Vertx vertx, VertxTestContext testContext)
      throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(PORT));
    client.get("/quotes/UNKNOWN").send().onComplete(testContext.succeeding(bufferHttpResponse -> {
      JsonObject json = bufferHttpResponse.bodyAsJsonObject();
      LOG.info("Response: {}", json);
      assertEquals(
          "{\"status\":\"FAILURE\",\"statusMsg\":\"{\\\"message\\\":\\\"quote for asset UNKNOWN not found\\\",\\\"path\\\":\\\"/quotes/UNKNOWN\\\"}\"}",
          json.encode());
      assertEquals(404, bufferHttpResponse.statusCode());
      testContext.completeNow();
    }));
  }
}
