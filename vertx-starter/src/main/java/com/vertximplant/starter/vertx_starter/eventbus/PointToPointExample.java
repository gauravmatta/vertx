package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.Vertx;

public class PointToPointExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Reciever());
  }
}
