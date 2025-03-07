package com.vertximplant.starter.vertx_big_board.router;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vertximplant.starter.vertx_big_board.api.AssetsRestAPI;
import com.vertximplant.starter.vertx_big_board.api.QuotesRestAPI;
import com.vertximplant.starter.vertx_big_board.api.WatchListRestAPI;
import com.vertximplant.starter.vertx_big_board.constants.HttpConstants;
import com.vertximplant.starter.vertx_big_board.handler.AssetsHandler;
import com.vertximplant.starter.vertx_big_board.handler.HealthHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.vertximplant.starter.vertx_big_board.constants.HttpConstants.HEALTH_ROUTE;
import static com.vertximplant.starter.vertx_big_board.constants.HttpConstants.HTTP_HEADER_CONTENT_VALUE_WITH_CHARSET;
import static org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.hc.core5.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

@Singleton
public class HttpRouter {

  private static final Logger LOG = LoggerFactory.getLogger(HttpRouter.class);
  @Inject
  AssetsHandler assetsHandler;
  @Inject
  HealthHandler healthHandler;
  @Inject
  AssetsRestAPI assetsRestAPI;
  @Inject
  QuotesRestAPI quotesRestAPI;
  @Inject
  WatchListRestAPI watchListRestAPI;

  private Router router;

  public Router init(Vertx vertx, Pool db) {
    this.router = Router.router(vertx);
    this.router.route().handler(BodyHandler.create().setBodyLimit(1024).setHandleFileUploads(true))
        .failureHandler(this::handleFailure);
    router.route().handler(getCorsHandler());
    router.route().handler(routingContext -> {
      routingContext.response().putHeader(HttpConstants.HTTP_HEADER_CONTENT_TYPE,
          HttpConstants.HTTP_HEADER_CONTENT_VALUE + "; charset=utf-8");
      routingContext.next();
    });
    this.assetsRouter();
    this.healthRouter();
    this.rootRouter();
    assetsRestAPI.attach(router, db);
    quotesRestAPI.attach(router, db);
    watchListRestAPI.attach(router);
    return router;
  }

  private void healthRouter() {
    router.route(HEALTH_ROUTE).handler(healthHandler);
    router.get(HEALTH_ROUTE).handler(healthHandler);
  }

  private void rootRouter() {
    router.route("/").handler(BodyHandler.create()).failureHandler(this::handleFailure);
    router.get("/").handler(routingContext -> {
      routingContext.response().setStatusCode(HttpConstants.HTTP_STATUS_200)
          .putHeader("content-type", "text/plain").end("Hello from Vert.x!");
    });
  }

  private void assetsRouter() {
    router.route("/assets").handler(BodyHandler.create()).failureHandler(this::handleFailure);
    router.get("/assets").handler(assetsHandler);
  }

  private CorsHandler getCorsHandler() {
    return CorsHandler.create().addOrigin("*").allowCredentials(true).allowedMethod(HttpMethod.GET)
        .allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.PUT)
        .allowedMethod(HttpMethod.OPTIONS).allowedHeader("*");
  }

  private void handleFailure(RoutingContext routingContext) {
    routingContext.response().setStatusCode(SC_INTERNAL_SERVER_ERROR)
        .putHeader(CONTENT_TYPE, HTTP_HEADER_CONTENT_VALUE_WITH_CHARSET).end();
  }

}
