package org.github.daroshchanka.infrastructure.db

import groovy.sql.Sql

class JdbcService {

  private static Properties config = new Properties().tap {
    load(getClass().getClassLoader().getResourceAsStream('db.properties'))
  }

  static Sql sql = Sql.newInstance(
      config.get('url'), config.get('username'), config.get('password'), config.get('driver')
  )

}
