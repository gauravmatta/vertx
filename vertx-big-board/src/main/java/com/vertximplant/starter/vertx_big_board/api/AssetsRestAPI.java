package com.vertximplant.starter.vertx_big_board.api;

import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.handler.AssetsHandler;
import io.vertx.ext.web.Router;

public class AssetsRestAPI {

  @Inject
  private AssetsHandler assetsHandler;

  public void attach(Router router){
    router.get("/assets_api").handler(assetsHandler::handle);
  }
}
