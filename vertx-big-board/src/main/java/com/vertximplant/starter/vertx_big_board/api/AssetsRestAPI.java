package com.vertximplant.starter.vertx_big_board.api;

import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.handler.CustomAssetHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.vertximplant.starter.vertx_big_board.constants.HttpConstants.HTTP_HEADER_CONTENT_VALUE_WITH_CHARSET;
import static org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.hc.core5.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

public class AssetsRestAPI {

  @Inject
  private CustomAssetHandler assetsHandler;

  private static final Logger LOG = LoggerFactory.getLogger(AssetsRestAPI.class);

  public void attach(Router router) {
    router.route("/assets_api").handler(BodyHandler.create()).failureHandler(this::handleFailure);
    router.get("/assets_api").handler(assetsHandler::handle);
  }

  private void handleFailure(RoutingContext routingContext) {
    if (routingContext.response().ended()) {
      return;
    }
    LOG.error("Route Error:", routingContext.failure());
    routingContext.response().setStatusCode(SC_INTERNAL_SERVER_ERROR)
        .putHeader(CONTENT_TYPE, HTTP_HEADER_CONTENT_VALUE_WITH_CHARSET)
        .end(new JsonObject().put("message", "Something went Wrong :(").toBuffer());
  }
}
