package com.vertximplant.starter.vertx_big_board.pojo.exception;

import lombok.Getter;

@Getter
public class AppError extends Throwable{
  private final Throwable throwable;
  private final String errorMessage;
  private int errorCode = 0;

  public AppError(Throwable throwable,String errorMessage){
    this.throwable=throwable;
    this.errorMessage= errorMessage;
  }

  public AppError(Throwable throwable,String errorMessage,int errorCode){
    this.throwable=throwable;
    this.errorMessage=errorMessage;
    this.errorCode=errorCode;
  }

}
