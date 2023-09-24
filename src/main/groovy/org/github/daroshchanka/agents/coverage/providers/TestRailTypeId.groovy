package org.github.daroshchanka.agents.coverage.providers

enum TestRailTypeId {

  ACCEPTANCE(1),
  ACCESSIBILITY(2),
  AUTOMATED(3),
  COMPATIBILITY(4),
  DESTRUCTIVE(5),
  FUNCTIONAL(6),
  OTHER(7),
  PERFORMANCE(8),
  REGRESSION(9),
  SECURITY(10),
  SMOKE(11),
  USABILITY(12),

  final int id

  TestRailTypeId(int id) {
    this.id = id
  }

}
