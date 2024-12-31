package com.vertximplant.starter.vertx_starter.eventbus;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class PublishSubscribeExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(Subscriber1.class.getName(), new DeploymentOptions().setInstances(2));
    vertx.deployVerticle(() -> new Subscriber(), new DeploymentOptions().setInstances(2));
  }
}
