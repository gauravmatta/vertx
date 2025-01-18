package com.vertximplant.starter.vertx_big_board.util;

import com.google.common.base.Stopwatch;
import com.vertximplant.starter.vertx_big_board.helper.GSONHelper;
import com.vertximplant.starter.vertx_big_board.pojo.LogEndIdentifier;
import com.vertximplant.starter.vertx_big_board.pojo.exception.AppError;
import com.vertximplant.starter.vertx_big_board.pojo.exception.Failure;
import com.vertximplant.starter.vertx_big_board.pojo.response.GenericResponse;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import java.util.HashMap;
import java.util.Map;
import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.vertximplant.starter.vertx_big_board.constants.HttpConstants.*;

public class AppResponseBuilder {

  public Map<String, String> buildResponseHeaders(String transid) {
    Map<String, String> requestHeaders = new HashMap<>();
    requestHeaders.put(CONTENT_TYPE, HTTP_HEADER_CONTENT_VALUE_WITH_CHARSET);
    requestHeaders.put(ACCEPT, HTTP_HEADER_CONTENT_VALUE);
    requestHeaders.put(HTTP_HEADER_CORRELATION_ID, transid);
    return requestHeaders;
  }

  public void sendResponse(HttpServerRequest httpServerRequest, int statusCode,
      String responseString, Map<String, String> headers, Stopwatch stopwatch) {
    HttpServerResponse httpServerResponse = httpServerRequest.response();
    if (headers != null) {
      headers.forEach((key, value) -> {
        if (key != null && value != null) {
          httpServerResponse.putHeader(key, value);
        }
      });
    }
    httpServerResponse.setStatusCode(statusCode).end(responseString);
  }

  public void exceptionResponseHandler(String transId, LogEndIdentifier logEndIdentifier,String eventName,Throwable t,HttpServerRequest httpServerRequest,String logAction,Stopwatch stopwatch){
    if(t instanceof Failure failure){
      int statusCode = failure.getCode();
      String statusMessage = failure.getStatusMsg();
      httpServerRequest.response().setStatusCode(statusCode).putHeader(CONTENT_TYPE,HTTP_HEADER_CONTENT_VALUE_WITH_CHARSET)
        .end(GSONHelper.gsonToString(GenericResponse.buildFailedRespone(statusMessage)));
    }else if(t instanceof AppError appError){
      int statusCode =appError.getErrorCode();
      String statusMessage = appError.getErrorMessage();
      httpServerRequest.response().setStatusCode(statusCode).putHeader(CONTENT_TYPE,HTTP_HEADER_CONTENT_VALUE_WITH_CHARSET)
        .end(GSONHelper.gsonToString(GenericResponse.buildFailedRespone(statusMessage)));
    }else {
httpServerRequest.response().setStatusCode(500).putHeader(CONTENT_TYPE,HTTP_HEADER_CONTENT_VALUE_WITH_CHARSET).end();
    }
  }

  public LogEndIdentifier buildLogEndIdentifier(String identifierName, String identifier) {
    LogEndIdentifier.LogEndIdentifierBuilder requestBuilder = LogEndIdentifier.builder();
    requestBuilder.identifierName(identifierName).identifier(identifier);
    return requestBuilder.build();
  }
}
