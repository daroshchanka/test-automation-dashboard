package org.github.daroshchanka.agents.coverage

import org.github.daroshchanka.HealthRecord
import org.github.daroshchanka.Platform

class CoverageHealthRecord implements HealthRecord {

  Metadata metadata
  Coverage overall
  Coverage byCritical
  BigDecimal health

  static class Metadata {
    Platform platform
  }

  static class Coverage {
    Integer total
    Integer automated
    Integer possible
    BigDecimal coverage
  }

}
