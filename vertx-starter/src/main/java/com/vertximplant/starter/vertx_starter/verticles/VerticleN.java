package com.vertximplant.starter.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class VerticleN extends AbstractVerticle {

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    System.out.println("Start " + getClass().getName() +" on thread "+ Thread.currentThread().getName());
    System.out.println("With config options "+ config().toString());
    startPromise.complete();
  }

}
