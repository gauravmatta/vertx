package com.vertximplant.starter.vertx_big_board.pojo.exception;

public class Failure extends RuntimeException {
  private int code;
  private String statusMsg;

  public Failure(int failureCode, String _statusMsg) {
    code = failureCode;
    statusMsg = _statusMsg;
    initCause(new RuntimeException());
  }

  public String getStatusMsg() {
    return statusMsg;
  }

  public int getCode() {
    return code;
  }
}
