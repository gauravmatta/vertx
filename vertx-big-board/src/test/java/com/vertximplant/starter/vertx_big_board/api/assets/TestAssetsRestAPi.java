package com.vertximplant.starter.vertx_big_board.api.assets;

import com.vertximplant.starter.vertx_big_board.api.broker.AbstractRestApiTest;
import com.vertximplant.starter.vertx_big_board.config.ConfigLoader;
import com.vertximplant.starter.vertx_big_board.verticles.MainVerticle;
import io.netty.handler.codec.http.HttpHeaderValues;
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
import static com.vertximplant.starter.vertx_big_board.constants.HttpConstants.HTTP_HEADER_CONTENT_TYPE;
import static com.vertximplant.starter.vertx_big_board.constants.HttpConstants.PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestAssetsRestAPi extends AbstractRestApiTest {

  private static final Logger LOG = LoggerFactory.getLogger(TestAssetsRestAPi.class);

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext testContext) {
    WebClient client = getWebClient(vertx);
    client.get("/assets").send().onComplete(testContext.succeeding(bufferHttpResponse -> {
      JsonArray objects = bufferHttpResponse.bodyAsJsonArray();
      LOG.info("Response: {}", objects);
      assertEquals(
          "[{\"symbol\":\"ICICIB22\"},{\"symbol\":\"CARRARO\"},{\"symbol\":\"COALINDIA\"},{\"symbol\":\"CPSEETF\"},{\"symbol\":\"EXIDEIND\"}]",
          objects.encode());
      assertEquals(200, bufferHttpResponse.statusCode());
      assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
          bufferHttpResponse.getHeader(HTTP_HEADER_CONTENT_TYPE));
      assertEquals("my-value", bufferHttpResponse.getHeader("my-header"));
      testContext.completeNow();
    }));
  }

  @Test
  void returns_all_assetsApi(Vertx vertx, VertxTestContext testContext) {
    WebClient client = getWebClient(vertx);
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

  private static WebClient getWebClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }
}
