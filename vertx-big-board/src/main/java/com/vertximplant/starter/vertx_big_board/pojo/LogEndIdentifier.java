package com.vertximplant.starter.vertx_big_board.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LogEndIdentifier {
  private String identifier;
  private String identifierName;
  private Throwable throwable;
}
