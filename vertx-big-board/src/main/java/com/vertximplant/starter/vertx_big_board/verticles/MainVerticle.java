package com.vertximplant.starter.vertx_big_board.verticles;

import com.vertximplant.starter.vertx_big_board.config.ConfigLoader;
import com.vertximplant.starter.vertx_big_board.db.migration.FlywayMigration;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    System.setProperty(ConfigLoader.SERVER_PORT, "9000");
    Vertx vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> {
      LOG.error("Unhandled:", error);
    });
    vertx.deployVerticle(new MainVerticle()).onFailure(err -> LOG.error("Failed to deploy:", err))
        .onSuccess(
            id -> LOG.info("Deployed {} with id {}", MainVerticle.class.getSimpleName(), id));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Available Number of Processers are {}", getProcessors());
    vertx.deployVerticle(VersionInfoVerticle.class.getName()).onFailure(startPromise::fail)
        .onSuccess(
            id -> LOG.info("Deployed {} with id {}", VersionInfoVerticle.class.getSimpleName(), id))
        .compose(next -> migrateDatabase()).onFailure(startPromise::fail)
        .onSuccess(id -> LOG.info("Migrated db schema to latest version"))
        .compose(next -> deployRestApiVerticle(startPromise));
  }

  private Future<Void> migrateDatabase() {
    return ConfigLoader.loadBrokerConfig(vertx).compose(config -> {
      return FlywayMigration.migrate(vertx, config.getDbConfig(),config.getMySqlDbConfig());
    });
  }

  private Future<String> deployRestApiVerticle(Promise<Void> startPromise) {
    return vertx
        .deployVerticle(RestAPIVerticle.class.getName(),
            new DeploymentOptions().setInstances(getProcessors()))
        .onFailure(startPromise::fail).onSuccess(id -> {
          LOG.info("Deployed {} with id {}", RestAPIVerticle.class.getSimpleName(), id);
          startPromise.complete();
        });
  }

  private static int getProcessors() {
    return Math.max(1, Runtime.getRuntime().availableProcessors());
  }
}
