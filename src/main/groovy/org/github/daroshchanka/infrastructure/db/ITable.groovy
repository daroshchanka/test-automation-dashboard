package org.github.daroshchanka.infrastructure.db

interface ITable<R> {

  void init()

  void insert(R record)
}
