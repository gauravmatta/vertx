package com.vertximplant.starter.vertx_big_board;

import com.google.inject.Guice;
import com.vertximplant.starter.vertx_big_board.router.HttpRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

import static com.vertximplant.starter.vertx_big_board.constants.HttpConstants.PORT;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);


  @Inject
  private HttpRouter httpRouter;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> {
      LOG.error("Unhandled:", error);
    });
    vertx.deployVerticle(new MainVerticle(), ar -> {
      if (ar.failed()) {
        LOG.error("Failed to deploy:", ar.cause());
        return;
      }
      LOG.info("Deployed:{}!", MainVerticle.class.getName());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    bootstrapGoogleGuice();
    bootStrapHttpServer(startPromise);
  }

  private void bootstrapGoogleGuice() {
    Guice.createInjector().injectMembers(this);
  }

  private void bootStrapHttpServer(Promise<Void> startPromise) {
    HttpServer httpServer = createHttpServer(startPromise);
    initHttpRequestHandler(httpServer);
    httpServer.listen(PORT, httpServerAsyncResult -> {
      setVerticleStartStatus(startPromise, httpServerAsyncResult);
    });
  }

  private HttpServer createHttpServer(Promise<Void> startPromise) {
    HttpServerOptions httpServerOptions = new HttpServerOptions();
    httpServerOptions.setSsl(false).setIdleTimeout(10).setIdleTimeoutUnit(TimeUnit.MINUTES);
    return vertx.createHttpServer(httpServerOptions);
  }

  private void setVerticleStartStatus(Promise<Void> startPromise,
      AsyncResult<HttpServer> httpServerAsyncResult) {
    if (httpServerAsyncResult.succeeded()) {
      startPromise.complete();
      LOG.info("HTTP server started on port 8888");
    } else {
      startPromise.fail(httpServerAsyncResult.cause());
    }
  }

  private void initHttpRequestHandler(HttpServer httpServer) {
    Router router = this.httpRouter.init(vertx);
    httpServer.requestHandler(router)
        .exceptionHandler(error -> LOG.error("HTTP Server error: ", error));
  }
}
