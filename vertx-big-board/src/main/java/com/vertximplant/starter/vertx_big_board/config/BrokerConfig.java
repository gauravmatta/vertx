package com.vertximplant.starter.vertx_big_board.config;

import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.util.Objects;

@Builder
@Value
@ToString
public class BrokerConfig {

  int serverPort;

  public static BrokerConfig from(final JsonObject config) {
    final Integer serverPort = config.getInteger(ConfigLoader.SERVER_PORT);
    if (Objects.isNull(serverPort)) {
      throw new RuntimeException("Server Port is not defined in the configuration");
    }
    return BrokerConfig.builder().serverPort(config.getInteger(ConfigLoader.SERVER_PORT)).build();
  }
}
