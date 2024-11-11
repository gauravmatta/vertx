package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.Vertx;

public class RequestResponseExample {
  public static void main(String[] args) {
    Vertx vertex = Vertx.vertx();
    vertex.deployVerticle(new RequestVerticle());
    vertex.deployVerticle(new ResponseVerticle());
  }
}
