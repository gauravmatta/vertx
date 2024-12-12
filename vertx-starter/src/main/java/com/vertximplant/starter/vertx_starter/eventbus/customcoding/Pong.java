package com.vertximplant.starter.vertx_starter.eventbus.customcoding;

public class Pong {
  private Integer id;
  public Pong(){

  }
  public Pong(final Integer id){
    this.id=id;
  }

  public Integer getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Pong{" +
      "id=" + id +
      '}';
  }
}
