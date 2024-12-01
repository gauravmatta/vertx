package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.Vertx;

public class RequestResponseJsonExample {
  public static void main(String[] args) {
    Vertx vertex = Vertx.vertx();
    vertex.deployVerticle(new RequestJsonVerticle());
    vertex.deployVerticle(new ResponseJsonVerticle());
  }
}
