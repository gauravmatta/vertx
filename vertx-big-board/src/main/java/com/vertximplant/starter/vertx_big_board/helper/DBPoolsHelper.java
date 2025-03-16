package com.vertximplant.starter.vertx_big_board.helper;

import com.vertximplant.starter.vertx_big_board.config.BrokerConfig;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public class DBPoolsHelper {

  public static Pool bootstrapMysqlPool(final BrokerConfig brokerConfig, final Vertx vertx) {
    final MySQLConnectOptions connectOptions = new MySQLConnectOptions()
      .setHost(brokerConfig.getDbConfig().getHost()).setPort(brokerConfig.getDbConfig().getPort())
      .setDatabase(brokerConfig.getDbConfig().getDatabase())
      .setUser(brokerConfig.getDbConfig().getUser())
      .setPassword(brokerConfig.getDbConfig().getPassword());
    PoolOptions poolOptions = new PoolOptions().setMaxSize(4);
    return Pool.pool(vertx, connectOptions, poolOptions);
  }

  public static Pool bootStrapPgPool(BrokerConfig brokerConfig,final Vertx vertx) {
    final PgConnectOptions connectOptions = new PgConnectOptions()
      .setHost(brokerConfig.getDbConfig().getHost()).setPort(brokerConfig.getDbConfig().getPort())
      .setDatabase(brokerConfig.getDbConfig().getDatabase())
      .setUser(brokerConfig.getDbConfig().getUser())
      .setPassword(brokerConfig.getDbConfig().getPassword());
    PoolOptions poolOptions = new PoolOptions().setMaxSize(4);
    return Pool.pool(vertx, connectOptions, poolOptions);
  }
}
