package com.vertximplant.starter.vertx_big_board.router;

import com.google.inject.Singleton;
import com.vertximplant.starter.vertx_big_board.constants.HttpConstants;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

@Singleton
public class HttpRouter {
  public static final String HEALTH_ROUTE = "/health";
  private Router router;

  public Router init(Vertx vertx) {
    this.router = Router.router(vertx);
    router.route().handler(getCorsHandler());
    router.route().handler(routingContext -> {
      routingContext.response().putHeader(HttpConstants.HTTP_HEADER_CONTENT_TYPE,
          HttpConstants.HTTP_HEADER_CONTENT_VALUE + "; charset=utf-8");
      routingContext.next();
    });
    this.healthRouter();
    return router;
  }

  private void healthRouter() {
    router.route(HEALTH_ROUTE).handler(this::health);
    router.get(HEALTH_ROUTE).handler(this::health);
  }

  private void health(RoutingContext routingContext) {
    routingContext.response().setStatusCode(HttpConstants.HTTP_STATUS_200)
        .putHeader(HttpConstants.HTTP_HEADER_CONTENT_TYPE,
            HttpConstants.HTTP_HEADER_CONTENT_VALUE + "; charset=utf-8")
        .end(Json.encodePrettily("Health OK"));
  }

  private void rootRouter() {
    router.route("/").handler(BodyHandler.create()).failureHandler(this::handleFailure);
    router.get("/").handler(routingContext -> {
      routingContext.response().setStatusCode(HttpConstants.HTTP_STATUS_200)
          .putHeader("content-type", "text/plain").end("Hello from Vert.x!");
    });
  }

  private CorsHandler getCorsHandler() {
    return CorsHandler.create().addOrigin("*").allowCredentials(true).allowedMethod(HttpMethod.GET)
        .allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.PUT)
        .allowedMethod(HttpMethod.OPTIONS).allowedHeader("*");
  }

//  private void handleFailure(RoutingContext routingContext) {
//    routingContext.response().setStatusCode(HttpConstants.HTTP_STATUS_ERROR_BAD_REQUEST);
//  }

}
