package org.github.daroshchanka.agents.coverage.providers

enum TestRailCasePriority {

  LOW(1),
  MEDIUM(2),
  HIGH(3),
  CRITICAL(4),

  final int id

  TestRailCasePriority(int id) {
    this.id = id
  }

}
