package com.vertximplant.starter.vertx_big_board.pojo;

import io.vertx.core.json.JsonObject;
import lombok.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class WatchList {
  List<Asset> assets;

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
