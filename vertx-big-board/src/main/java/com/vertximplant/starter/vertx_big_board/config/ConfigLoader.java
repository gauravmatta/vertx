package com.vertximplant.starter.vertx_big_board.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConfigLoader {
  public static final String CONFIG_FILE = "application.yml";
  private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);
  // Exposed Envirnoment Variables
  public static final String SERVER_PORT = "SERVER_PORT";
  public static final String DB_HOST = "DB_HOST";
  public static final String DB_PORT = "DB_PORT";
  public static final String DB_DATABASE = "DB_DATABASE";
  public static final String DB_USER = "DB_USER";
  public static final String DB_PASSWORD = "DB_PASSWORD";
  public static final String MySql_HOST = "MySql_HOST";
  public static final String MySql_PORT = "MySql_PORT";
  public static final String MySql_DATABASE = "MySql_DATABASE";
  public static final String MySql_USER = "MySql_USER";
  public static final String MySql_PASSWORD = "MySql_PASSWORD";
  static final List<String> EXPOSED_ENVIRONMENT_VARIABLES =
      List.of(SERVER_PORT, DB_HOST, DB_PORT, DB_DATABASE, DB_USER, DB_PASSWORD,MySql_HOST, MySql_PORT, MySql_DATABASE, MySql_USER, MySql_PASSWORD);


  public static Future<BrokerConfig> loadBrokerConfig(Vertx vertx) {

    final JsonArray exposedKeys = new JsonArray();
    EXPOSED_ENVIRONMENT_VARIABLES.forEach(exposedKeys::add);
    LOG.debug("Fetch Configuration from Exposed keys: {}", exposedKeys);

    ConfigStoreOptions envStore = new ConfigStoreOptions().setType("env")
        .setConfig(new JsonObject().put("keys", exposedKeys));

    ConfigStoreOptions propertyStore =
        new ConfigStoreOptions().setType("sys").setConfig(new JsonObject().put("cache", false));

    ConfigStoreOptions ymlStore = new ConfigStoreOptions().setType("file").setFormat("yaml")
        .setConfig(new JsonObject().put("path", CONFIG_FILE));

    ConfigRetriever configRetriever = ConfigRetriever.create(vertx,
        new ConfigRetrieverOptions().addStore(ymlStore).addStore(propertyStore).addStore(envStore));

    return configRetriever.getConfig().map(BrokerConfig::from);
  }
}
