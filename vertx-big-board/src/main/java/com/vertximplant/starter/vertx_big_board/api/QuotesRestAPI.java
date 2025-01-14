package com.vertximplant.starter.vertx_big_board.api;

import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.handler.QuotesHandler;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotesRestAPI {
  private static final Logger LOG = LoggerFactory.getLogger(QuotesRestAPI.class);

  @Inject
  private QuotesHandler quotesHandler;

  public void attach(Router router) {
    router.get("/quotes/:asset").handler(quotesHandler::handle);
  }
}
