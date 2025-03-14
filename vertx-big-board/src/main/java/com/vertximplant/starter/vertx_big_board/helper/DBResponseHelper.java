package com.vertximplant.starter.vertx_big_board.helper;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import com.vertximplant.starter.vertx_big_board.util.AppResponseBuilder;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.vertximplant.starter.vertx_big_board.helper.GSONHelper.gsonToString;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;

public class DBResponseHelper {

  @Inject
  private AppResponseBuilder responseBuilder;

  private static final Logger LOG = LoggerFactory.getLogger(DBResponseHelper.class);

  public static Handler<Throwable> errorHandler(RoutingContext routingContext, String message) {
    return error -> {
      LOG.error("DB Failure: ", error);
      routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(new JsonObject().put("message", message).put("path", routingContext.normalizedPath())
              .toBuffer());
    };
  }

  public void handleEmptyResponse(String transid, RoutingContext routingContext, String identifier,
      String eventName, Stopwatch stopwatch) {
    JsonObject logObj = new JsonObject();
    logObj.put("transid", transid);
    logObj.put("identifier", identifier);
    logObj.put("eventName", eventName);
    LOG.info(logObj.toString());
    responseBuilder.sendResponse(routingContext.request(), HttpResponseStatus.NO_CONTENT.code(), "",
        responseBuilder.buildResponseHeaders(transid), stopwatch);
  }

  public void handleErrorResponse(String transid, RoutingContext routingContext, String identifier,
      Throwable throwable, String eventName, Stopwatch stopwatch) {
    responseBuilder.exceptionResponseHandler(transid,
        responseBuilder.buildLogEndIdentifier("user_id", identifier), eventName, throwable,
        routingContext.request(), ((Failure) throwable).getStatusMsg(), stopwatch);
  }

  public void handleStringResponse(String transid, RoutingContext routingContext, String object,
      Stopwatch stopwatch) {
    responseBuilder.sendResponse(routingContext.request(), SC_OK, object,
        responseBuilder.buildResponseHeaders(transid), stopwatch);
  }

  public void handleSuccessResponse(String transid, RoutingContext routingContext, Object object,
      Stopwatch stopwatch) {
    responseBuilder.sendResponse(routingContext.request(), SC_OK, gsonToString(object),
        responseBuilder.buildResponseHeaders(transid), stopwatch);
  }

  public void handleSuccessJsonObjectResponse(String transid, RoutingContext routingContext,
      JsonObject object, Stopwatch stopwatch) {
    responseBuilder.sendJsonResponse(routingContext.request(), SC_OK, object.toString(),
        responseBuilder.buildResponseHeaders(transid), stopwatch);
  }

  public void handleSuccessJsonArrayResponse(String transid, RoutingContext routingContext,
      JsonArray array, Stopwatch stopwatch) {
    responseBuilder.sendResponse(routingContext.request(), SC_OK, array.toString(),
        responseBuilder.buildResponseHeaders(transid), stopwatch);
  }
}
