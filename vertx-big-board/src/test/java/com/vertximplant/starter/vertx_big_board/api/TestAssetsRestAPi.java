package com.vertximplant.starter.vertx_big_board.api;

import com.vertximplant.starter.vertx_big_board.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
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
public class TestAssetsRestAPi {

  private static final Logger LOG = LoggerFactory.getLogger(TestAssetsRestAPi.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle())
        .onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(PORT));
    client.get("/assets").send().onComplete(testContext.succeeding(bufferHttpResponse -> {
      JsonArray objects = bufferHttpResponse.bodyAsJsonArray();
      LOG.info("Response: {}", objects);
      assertEquals(
          "[{\"symbol\":\"ICICIB22\"},{\"symbol\":\"CARRARO\"},{\"symbol\":\"COALINDIA\"},{\"symbol\":\"CPSEETF\"},{\"symbol\":\"EXIDEIND\"}]",
          objects.encode());
      assertEquals(200, bufferHttpResponse.statusCode());
      testContext.completeNow();
    }));
  }

  @Test
  void returns_all_assetsApi(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(PORT));
    client.get("/assets_api").send().onComplete(testContext.succeeding(bufferHttpResponse -> {
      JsonArray objects = bufferHttpResponse.bodyAsJsonArray();
      LOG.info("Response: {}", objects);
      assertEquals(
        "[{\"name\":\"AADHARHFC\"},{\"name\":\"ACC\"},{\"name\":\"AFCONS\"},{\"name\":\"ARE&M\"},{\"name\":\"ASIANPAINT\"},{\"name\":\"BAJAJ-AUTO\"},{\"name\":\"BANKINDIA\"},{\"name\":\"BEL\"},{\"name\":\"BPCL\"}]",
        objects.encode());
      assertEquals(200, bufferHttpResponse.statusCode());
      testContext.completeNow();
    }));
  }
}
