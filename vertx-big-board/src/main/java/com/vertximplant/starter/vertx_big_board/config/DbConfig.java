package com.vertximplant.starter.vertx_big_board.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DbConfig {
  String host="localhost";
  int port = 5432;
  String database = "vertx-big-board";
  String user = "postgres";
  String password = "postgres";

  @Override
  public String toString() {
    return "DbConfig{" +
      "host='" + host + '\'' +
      ", port=" + port +
      ", database='" + database + '\'' +
      ", user='" + user + '\'' +
      '}';
  }
}
