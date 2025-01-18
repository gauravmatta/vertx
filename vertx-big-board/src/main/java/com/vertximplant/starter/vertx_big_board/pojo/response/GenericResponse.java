package com.vertximplant.starter.vertx_big_board.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
  private String status;
  private String statusMsg;

  public static GenericResponse buildSuccessResponse() {
    return new GenericResponse("SUCCESS", "SUCCESS");
  }

  public static GenericResponse buildFailedRespone(String statusMsg) {
    return new GenericResponse("FAILURE", statusMsg);
  }
}
