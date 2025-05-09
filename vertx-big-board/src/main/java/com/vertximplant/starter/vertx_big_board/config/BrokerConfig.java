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
  String version;
  DbConfig dbConfig;
  MySqlDbConfig mySqlDbConfig;

  public static BrokerConfig from(final JsonObject config) {
    final Integer serverPort = config.getInteger(ConfigLoader.SERVER_PORT);
    if (Objects.isNull(serverPort)) {
      throw new RuntimeException("Server Port is not defined in the configuration");
    }
    final String version = config.getString("version");
    if (Objects.isNull(version)) {
      throw new RuntimeException("Version is not configured in config file!");
    }
    return BrokerConfig.builder().serverPort(serverPort).version(version)
        .dbConfig(parseDbConfig(config)).mySqlDbConfig(parseMySqlDbConfig(config)).build();
  }

  private static DbConfig parseDbConfig(final JsonObject config) {
    return DbConfig.builder().host(config.getString(ConfigLoader.DB_HOST))
        .port(config.getInteger(ConfigLoader.DB_PORT))
        .database(config.getString(ConfigLoader.DB_DATABASE))
        .user(config.getString(ConfigLoader.DB_USER))
        .password(config.getString(ConfigLoader.DB_PASSWORD)).build();
  }

  private static MySqlDbConfig parseMySqlDbConfig(final JsonObject config) {
    return MySqlDbConfig.builder().host(config.getString(ConfigLoader.MySql_HOST))
      .port(config.getInteger(ConfigLoader.MySql_PORT))
      .database(config.getString(ConfigLoader.MySql_DATABASE))
      .user(config.getString(ConfigLoader.MySql_USER))
      .password(config.getString(ConfigLoader.MySql_PASSWORD)).build();
  }
}
