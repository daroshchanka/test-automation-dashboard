package org.github.daroshchanka.agents


import org.github.daroshchanka.HealthRecord

interface IHealthAgent<Q extends IQueryCommand, R extends HealthRecord> {

  R query(Q command)

  void store(R record)

  default void scanHealth(Q command) {
    store(query(command))
  }

}
