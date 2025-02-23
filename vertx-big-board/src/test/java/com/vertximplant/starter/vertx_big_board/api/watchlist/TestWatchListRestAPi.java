package com.vertximplant.starter.vertx_big_board.api.watchlist;

import com.vertximplant.starter.vertx_big_board.api.broker.AbstractRestApiTest;
import com.vertximplant.starter.vertx_big_board.pojo.Asset;
import com.vertximplant.starter.vertx_big_board.pojo.WatchList;
import io.vertx.core.Future;
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

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestWatchListRestAPi extends AbstractRestApiTest {

  private static final Logger LOG = LoggerFactory.getLogger(TestWatchListRestAPi.class);

  @Test
  void adds_and_returns_watchlist_for_account(Vertx vertx, VertxTestContext testContext)
      throws Throwable {
    WebClient client = getWebClient(vertx);
    UUID accountId = UUID.randomUUID();
    client.put("/account/watchlist/" + accountId).sendJsonObject(getBody())
        .onComplete(testContext.succeeding(bufferHttpResponse -> {
          JsonObject json = bufferHttpResponse.bodyAsJsonObject();
          LOG.info("Response PUT: {}", json);
          assertEquals(
              "{\"assets\":[{\"name\":\"LIQUIDCASE\"},{\"name\":\"ZEEL\"},{\"name\":\"YESBANK\"},{\"name\":\"WAAREEENER\"},{\"name\":\"IDEA\"},{\"name\":\"VEDL\"},{\"name\":\"UTIAMC\"},{\"name\":\"TORNTPOWER\"},{\"name\":\"TATATECH\"},{\"name\":\"TATAPOWER\"}]}",
              json.encode());
          assertEquals(200, bufferHttpResponse.statusCode());
        })).compose(next -> {
          client.get("/account/watchlist/" + accountId).send()
              .onComplete(testContext.succeeding(bufferHttpResponse -> {
                JsonObject json = bufferHttpResponse.bodyAsJsonObject();
                LOG.info("Response GET: {}", json);
                assertEquals(
                    "{\"assets\":[{\"name\":\"LIQUIDCASE\"},{\"name\":\"ZEEL\"},{\"name\":\"YESBANK\"},{\"name\":\"WAAREEENER\"},{\"name\":\"IDEA\"},{\"name\":\"VEDL\"},{\"name\":\"UTIAMC\"},{\"name\":\"TORNTPOWER\"},{\"name\":\"TATATECH\"},{\"name\":\"TATAPOWER\"}]}",
                    json.encode());
                assertEquals(200, bufferHttpResponse.statusCode());
                testContext.completeNow();
              }));
          return Future.succeededFuture();
        });
  }

  @Test
  void adds_and_deletes_watchlist_for_account(Vertx vertx, VertxTestContext testContext) {
    WebClient client = getWebClient(vertx);
    UUID accountId = UUID.randomUUID();
    client.put("/account/watchlist/" + accountId).sendJsonObject(getBody())
        .onComplete(testContext.succeeding(bufferHttpResponse -> {
          JsonObject json = bufferHttpResponse.bodyAsJsonObject();
          LOG.info("Response PUT: {}", json);
          assertEquals(
              "{\"assets\":[{\"name\":\"LIQUIDCASE\"},{\"name\":\"ZEEL\"},{\"name\":\"YESBANK\"},{\"name\":\"WAAREEENER\"},{\"name\":\"IDEA\"},{\"name\":\"VEDL\"},{\"name\":\"UTIAMC\"},{\"name\":\"TORNTPOWER\"},{\"name\":\"TATATECH\"},{\"name\":\"TATAPOWER\"}]}",
              json.encode());
          assertEquals(200, bufferHttpResponse.statusCode());
        })).compose(next -> {
          client.delete("/account/watchlist/" + accountId).send()
              .onComplete(testContext.succeeding(bufferHttpResponse -> {
                JsonObject json = bufferHttpResponse.bodyAsJsonObject();
                LOG.info("Response DELETE: {}", json);
                assertEquals(
                    "{\"assets\":[{\"name\":\"LIQUIDCASE\"},{\"name\":\"ZEEL\"},{\"name\":\"YESBANK\"},{\"name\":\"WAAREEENER\"},{\"name\":\"IDEA\"},{\"name\":\"VEDL\"},{\"name\":\"UTIAMC\"},{\"name\":\"TORNTPOWER\"},{\"name\":\"TATATECH\"},{\"name\":\"TATAPOWER\"}]}",
                    json.encode());
                assertEquals(200, bufferHttpResponse.statusCode());
                testContext.completeNow();
              }));
          return Future.succeededFuture();
        });
  }

  private static JsonObject getBody() {
    return new WatchList(
        Arrays.asList(new Asset("LIQUIDCASE"), new Asset("ZEEL"), new Asset("YESBANK"),
            new Asset("WAAREEENER"), new Asset("IDEA"), new Asset("VEDL"), new Asset("UTIAMC"),
            new Asset("TORNTPOWER"), new Asset("TATATECH"), new Asset("TATAPOWER"))).toJsonObject();
  }

  private static WebClient getWebClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }
}
