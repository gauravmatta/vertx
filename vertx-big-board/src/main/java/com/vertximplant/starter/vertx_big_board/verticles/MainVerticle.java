package com.vertximplant.starter.vertx_big_board.verticles;

import com.google.inject.Guice;
import com.vertximplant.starter.vertx_big_board.router.HttpRouter;
import io.vertx.core.*;
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
    vertx.deployVerticle(new MainVerticle())
      .onFailure(err -> LOG.error("Failed to deploy:",err))
      .onSuccess(id ->LOG.info("Deployed {} with id {}", MainVerticle.class.getSimpleName(),id));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Available Number of Processers are {}", getProcessors());
    vertx.deployVerticle(RestAPIVerticle.class.getName(),new DeploymentOptions().setInstances(
        getProcessors()
      ))
      .onFailure(startPromise::fail).onSuccess(id->{
        LOG.info("Deployed {} with id {}",RestAPIVerticle.class.getSimpleName(),id);
        startPromise.complete();
      });
  }

  private static int getProcessors() {
    return Math.max(1,Runtime.getRuntime().availableProcessors());
  }
}
