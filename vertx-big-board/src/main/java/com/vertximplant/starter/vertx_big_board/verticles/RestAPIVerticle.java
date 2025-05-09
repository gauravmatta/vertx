package com.vertximplant.starter.vertx_big_board.verticles;

import com.google.inject.Guice;
import com.vertximplant.starter.vertx_big_board.config.BrokerConfig;
import com.vertximplant.starter.vertx_big_board.config.ConfigLoader;
import com.vertximplant.starter.vertx_big_board.helper.DBPoolsHelper;
import com.vertximplant.starter.vertx_big_board.router.HttpRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.sqlclient.Pool;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RestAPIVerticle extends AbstractVerticle {

  @Inject
  private HttpRouter httpRouter;

  private static final Logger LOG = LoggerFactory.getLogger(RestAPIVerticle.class);

  private Pool db;
  private Pool mySqlDb;

  @Override
  public void start(Promise<Void> startPromise) {
    ConfigLoader.loadBrokerConfig(vertx).onFailure(startPromise::fail).onSuccess(brokerConfig -> {
      LOG.info("Retrived Configuration From Broker Config: {}", brokerConfig);
      bootstrapGoogleGuice();
     db= DBPoolsHelper.bootStrapPgPool(brokerConfig,vertx);
     mySqlDb = DBPoolsHelper.bootstrapMysqlPool(brokerConfig,vertx);
      bootStrapHttpServer(startPromise, brokerConfig);
    });
  }

  private void bootstrapGoogleGuice() {
    Guice.createInjector().injectMembers(this);
  }

  private void bootStrapHttpServer(Promise<Void> startPromise, BrokerConfig brokerConfig) {
    HttpServer httpServer = createHttpServer(startPromise);
    initHttpRequestHandler(httpServer);
    httpServer.listen(brokerConfig.getServerPort(), httpServerAsyncResult -> {
      setVerticleStartStatus(startPromise, httpServerAsyncResult, brokerConfig);
    });
  }

  private HttpServer createHttpServer(Promise<Void> startPromise) {
    HttpServerOptions httpServerOptions = new HttpServerOptions();
    httpServerOptions.setSsl(false).setIdleTimeout(10).setIdleTimeoutUnit(TimeUnit.MINUTES);
    return vertx.createHttpServer(httpServerOptions);
  }

  private void setVerticleStartStatus(Promise<Void> startPromise,
      AsyncResult<HttpServer> httpServerAsyncResult, BrokerConfig brokerConfig) {
    if (httpServerAsyncResult.succeeded()) {
      startPromise.complete();
      LOG.info("HTTP server started on port {}", brokerConfig.getServerPort());
    } else {
      startPromise.fail(httpServerAsyncResult.cause());
    }
  }

  private void initHttpRequestHandler(HttpServer httpServer) {
    Router router = this.httpRouter.init(vertx, db,mySqlDb);
    httpServer.requestHandler(router)
        .exceptionHandler(error -> LOG.error("HTTP Server error: ", error));
  }
}
