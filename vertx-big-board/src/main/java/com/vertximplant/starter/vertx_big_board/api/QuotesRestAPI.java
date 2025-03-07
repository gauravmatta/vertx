package com.vertximplant.starter.vertx_big_board.api;

import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.handler.GetQuoteFromDatabaseHandler;
import com.vertximplant.starter.vertx_big_board.handler.QuotesHandler;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotesRestAPI {
  private static final Logger LOG = LoggerFactory.getLogger(QuotesRestAPI.class);

  @Inject
  private QuotesHandler quotesHandler;

  @Inject
  private GetQuoteFromDatabaseHandler getQuoteFromDatabaseHandler;

  public void attach(Router router, final Pool db) {
    router.get("/quotes/:asset").handler(quotesHandler);
    router.get("/pg/quotes/:asset").handler(routingContext -> getQuoteFromDatabaseHandler.handle(routingContext,db));
  }
}
