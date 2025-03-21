package com.vertximplant.mutiny.vertx_mutiny.db;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.impl.PgPoolOptions;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalTime;

public class VertxMutinyReactiveSQL extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(VertxMutinyReactiveSQL.class);


  public static void main(String[] args) {
    DeploymentOptions options =
        new DeploymentOptions().setConfig(new JsonObject().put("port", 5432));
    Vertx.vertx().deployVerticle(VertxMutinyReactiveSQL::new, options).subscribe()
        .with(id -> LOG.info("Started: {}", id));
  }

  @Override
  public Uni<Void> asyncStart() {
    Router router = Router.router(vertx);
    vertx.setPeriodic(5000L, item -> LOG.info("{}", LocalTime.now().getMinute()));
    Pool db = createPgPool(config());
    router.route().failureHandler(this::failureHandler);
    router.get("/assets").respond(routingContext -> executeQuery(db));
    router.get().respond(this::rootHandler);
    return vertx.createHttpServer().requestHandler(router).listen(8111).replaceWithVoid();
  }

  private Uni<JsonArray> executeQuery(Pool db) {
    LOG.info("Executing DB query to find all users...");
    return db.query("SELECT * FROM broker.assets").execute().onItem().transform(rows -> {
      JsonArray data = new JsonArray();
      for (Row row : rows) {
        data.add(row.toJson());
      }
      LOG.info("Return data: {}", data);
      return data;
    }).onFailure().invoke(failure -> LOG.error("Failed query: ", failure)).onFailure()
        .recoverWithItem(new JsonArray());
  }

  private Pool createPgPool(JsonObject config) {
    PgConnectOptions connectOptions = new PgConnectOptions().setHost("localhost")
        .setPort(config.getInteger("port")).setDatabase(EmbeddedPostgres.DATABASE_NAME)
        .setUser(EmbeddedPostgres.USERNAME).setPassword(EmbeddedPostgres.PASSWORD);

    PoolOptions poolOptions = new PgPoolOptions().setMaxSize(5);

    return Pool.pool(vertx, connectOptions, poolOptions);
  }

  private Uni<String> rootHandler(RoutingContext routingContext) {
    return Uni.createFrom().item("Hello").onItem().transform(item -> item + " Mutiny!").onItem()
        .transform(String::toUpperCase);
  }

  private void failureHandler(RoutingContext routingContext) {
    routingContext.response().setStatusCode(500).endAndForget("Something went wrong :(");
  }
}
