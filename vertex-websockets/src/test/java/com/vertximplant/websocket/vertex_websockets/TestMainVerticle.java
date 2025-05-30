package com.vertximplant.websocket.vertex_websockets;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocketConnectOptions;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  public static final int EXPECTED_MESSAGES = 5;
  private static final Logger LOG = LoggerFactory.getLogger(TestMainVerticle.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle()).onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  @Timeout(value = 10,timeUnit = TimeUnit.SECONDS)
  void can_connect_to_web_socket_server(Vertx vertx, VertxTestContext context) throws Throwable {
    HttpClient client = vertx.createHttpClient();
   client.webSocket(8900,"localhost",WebSocketHandler.PATH)
       .onFailure(context::failNow)
         .onComplete(
           context.succeeding(webSocket -> {
             webSocket.handler(data ->{
               final String recievedData = data.toString();
               LOG.info("Recieved Message: {}",recievedData);
               assertEquals("Connected!",recievedData);
               client.close();
               context.completeNow();
             });
           })
         );
  }

  @Test
  @Timeout(value = 10,timeUnit = TimeUnit.SECONDS)
  void can_receive_multiple_messages(Vertx vertx, VertxTestContext context) throws Throwable {
    HttpClient client = vertx.createHttpClient();
    final AtomicInteger counter = new AtomicInteger(0);
    client.webSocket(new WebSocketConnectOptions()
        .setHost("localhost")
        .setPort(8900)
        .setURI(WebSocketHandler.PATH)
      )
      .onFailure(context::failNow)
      .onComplete(
        context.succeeding(webSocket -> {
          webSocket.handler(data ->{
            final String recievedData = data.toString();
            LOG.info("Received Message: {}",recievedData);
            int currentValue = counter.getAndIncrement();
            if(currentValue > EXPECTED_MESSAGES) {
              client.close();
              context.completeNow();
            } else {
              LOG.info("not enough messages yet... ({}/{})",currentValue,EXPECTED_MESSAGES);
            }
          });
        })
      );
  }
  }
