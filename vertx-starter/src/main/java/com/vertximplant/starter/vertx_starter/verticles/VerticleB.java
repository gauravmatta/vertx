package com.vertximplant.starter.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class VerticleB extends AbstractVerticle {

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    System.out.println("Start " + getClass().getName());
    startPromise.complete();
  }

}
