package com.vertximplant.starter.vertx_big_board.pojo;

import io.vertx.core.json.JsonObject;
import lombok.Value;
import java.util.List;

@Value
public class WatchList {
  List<Asset> assets;

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
