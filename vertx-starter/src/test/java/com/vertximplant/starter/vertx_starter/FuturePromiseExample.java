package com.vertximplant.starter.vertx_starter;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {

  private static final Logger LOG = LoggerFactory.getLogger(FuturePromiseExample.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext context){
   final Promise<String> promise = Promise.promise();
   LOG.debug("Start");
   vertx.setTimer(500,id ->{
     promise.complete("Success");
     LOG.debug("Success");
     context.completeNow();
   });
   LOG.debug("End");
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext context){
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(500,id ->{
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Failed");
      context.completeNow();
    });
    LOG.debug("End");
  }

  @Test
  void future_success(Vertx vertx,VertxTestContext context){
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(500,id ->{
      promise.complete("Promise Success");
      LOG.debug("Timer Done");
    });
    Future<String> future = promise.future();
    future.onSuccess(result ->{
      LOG.debug("Result: {}",result);
      LOG.debug("END");
      context.completeNow();
    })
      .onFailure(context::failNow);
  }

  @Test
  void future_failure(Vertx vertx,VertxTestContext context){
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(500,id ->{
      promise.fail(new RuntimeException("Failed"));
      LOG.debug("Timer Done");
    });
    Future<String> future = promise.future();
    future.onSuccess(result ->{
        context.failNow("Move To Failed");
      })
      .onFailure(error ->{
        LOG.debug("Result: {}",error);
        LOG.debug("END");
        context.completeNow();
      });
  }

  @Test
  void future_map(Vertx vertx,VertxTestContext context){
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(500,id ->{
      promise.complete("Promise Success");
      LOG.debug("Timer Done");
    });
    Future<String> future = promise.future();
    future
      .map(asString->{
        LOG.debug("Map String To Json Object");
        return new JsonObject().put("key",asString);
      })
      .map(jsonObject->new JsonArray().add(jsonObject))
      .onSuccess(result ->{
        LOG.debug("Result: {} of type {}",result,result.getClass().getName());
        LOG.debug("END");
        context.completeNow();
      })
      .onFailure(context::failNow);
  }

  @Test
  void future_coordination(Vertx vertx,VertxTestContext context){
    vertx.createHttpServer()
      .requestHandler(request -> LOG.debug("{}",request))
      .listen(10000)
      .compose(server -> {
        LOG.debug("Another Task");
        return Future.succeededFuture(server);
      })
      .compose(server->{
        LOG.debug("Even More");
        return Future.succeededFuture(server);
      })
      .onFailure(context::failNow)
      .onSuccess(server->{
        LOG.debug("Server started on Port {}",server.actualPort());
        context.completeNow();
      });
  }

  @Test
  void future_composition_all(Vertx vertx,VertxTestContext context){
    Promise<Void> one = Promise.<Void>promise();
    Promise<Void> two = Promise.<Void>promise();
    Promise<Void> three = Promise.<Void>promise();

    Future<Void> futureOne = one.future();
    Future<Void> futureTwo = two.future();
    Future<Void> futureThree = three.future();

    CompositeFuture.all(futureOne,futureTwo,futureThree)
      .onFailure(context::failNow)
      .onSuccess(result -> {
        LOG.debug("Success");
        context.completeNow();
      });

    vertx.setTimer(500,id->{
      one.complete();
      two.complete();
      three.complete();
    });
  }

  @Test
  void future_composition_any(Vertx vertx,VertxTestContext context){
    Promise<Void> one = Promise.<Void>promise();
    Promise<Void> two = Promise.<Void>promise();
    Promise<Void> three = Promise.<Void>promise();

    Future<Void> futureOne = one.future();
    Future<Void> futureTwo = two.future();
    Future<Void> futureThree = three.future();

    CompositeFuture.any(futureOne,futureTwo,futureThree)
      .onFailure(context::failNow)
      .onSuccess(result -> {
        LOG.debug("Success");
        context.completeNow();
      });

    vertx.setTimer(500,id->{
      one.complete();
      two.complete();
      three.fail("Three Failed");
    });
  }

}
