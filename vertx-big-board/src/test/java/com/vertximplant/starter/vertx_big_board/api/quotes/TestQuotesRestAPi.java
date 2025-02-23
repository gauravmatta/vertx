package com.vertximplant.starter.vertx_big_board.api.quotes;

import com.vertximplant.starter.vertx_big_board.api.broker.AbstractRestApiTest;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestQuotesRestAPi extends AbstractRestApiTest {

  private static final Logger LOG = LoggerFactory.getLogger(TestQuotesRestAPi.class);

  @Test
  void returns_quote_for_asset(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = getWebClient(vertx);
    client.get("/quotes/BANKINDIA").send().onComplete(testContext.succeeding(bufferHttpResponse -> {
      JsonObject json = bufferHttpResponse.bodyAsJsonObject();
      LOG.info("Response: {}", json);
      assertEquals("{\"name\":\"BANKINDIA\"}", json.getJsonObject("asset").encode());
      assertEquals(200, bufferHttpResponse.statusCode());
      testContext.completeNow();
    }));
  }

  private static WebClient getWebClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }

  @Test
  void returns_not_found_for_unknown_asset(Vertx vertx, VertxTestContext testContext)
      throws Throwable {
    WebClient client = getWebClient(vertx);
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
