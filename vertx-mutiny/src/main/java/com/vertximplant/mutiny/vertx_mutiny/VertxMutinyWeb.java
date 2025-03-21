package com.vertximplant.mutiny.vertx_mutiny;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxMutinyWeb extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(VertxMutinyWeb.class);

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new VertxMutinyWeb()).subscribe()
        .with(id -> LOG.info("Started: {}", id));
  }

  @Override
  public Uni<Void> asyncStart() {
    Router router = Router.router(vertx);
    router.route().failureHandler(this::failureHandler);
    router.get("/users").respond(this::getUsers);
    router.get().respond(this::rootHandler);
    return vertx.createHttpServer().requestHandler(router).listen(8111).replaceWithVoid();
  }

  private Uni<String> rootHandler(RoutingContext routingContext) {
    return Uni.createFrom().item("Hello").onItem().transform(item -> item + " Mutiny!").onItem()
        .transform(String::toUpperCase);
  }

  private void failureHandler(RoutingContext routingContext) {
    routingContext.response().setStatusCode(500).endAndForget("Something went wrong :(");
  }

  private Uni<JsonArray> getUsers(RoutingContext routingContext) {
    final JsonArray responseBody = new JsonArray();
    responseBody.add(new JsonObject().put("name", "Gaurav"));
    responseBody.add(new JsonObject().put("name", "Raghavi"));
    return Uni.createFrom().item(responseBody);
  }
}
