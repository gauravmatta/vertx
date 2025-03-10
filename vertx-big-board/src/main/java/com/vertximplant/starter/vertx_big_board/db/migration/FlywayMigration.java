package com.vertximplant.starter.vertx_big_board.db.migration;

import com.vertximplant.starter.vertx_big_board.config.DbConfig;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlywayMigration {

  private static final Logger LOG = LoggerFactory.getLogger(FlywayMigration.class);

  public static Future<Void> migrate(Vertx vertx, DbConfig dbConfig) {
    // Running on a blocking Thread
    LOG.debug("DB Config: {}", dbConfig);
    return vertx.<Void>executeBlocking(promise -> {
      execute(dbConfig);
      promise.complete();
    }).onFailure(err -> LOG.error("Failed to Migrte database schema: ", err));
  }

  private static void execute(DbConfig dbConfig) {
    final String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", dbConfig.getHost(),
        dbConfig.getPort(), dbConfig.getDatabase());
    LOG.debug("Migrating DB schema using jdbc url: {}", jdbcUrl);

    final Flyway flyway =
        Flyway.configure().dataSource(jdbcUrl, dbConfig.getUser(), dbConfig.getPassword())
            .schemas("broker").defaultSchema("broker").load();

    Optional<MigrationInfo> current = Optional.ofNullable(flyway.info().current());
    current.ifPresent(info -> LOG.info("db schema is at version: {}", info.getVersion()));
    MigrationInfo[] pendingMigrations = flyway.info().pending();
    LOG.debug("Pending migrations are: {}", printMigrtions(pendingMigrations));
    flyway.migrate();
  }

  private static String printMigrtions(final MigrationInfo[] pendingMigrations) {
    if (Objects.isNull(pendingMigrations)) {
      return "[]";
    }
    return Arrays.stream(pendingMigrations)
        .map(each -> each.getVersion() + " - " + each.getDescription())
        .collect(Collectors.joining(",", "[", "]"));

  }
}
