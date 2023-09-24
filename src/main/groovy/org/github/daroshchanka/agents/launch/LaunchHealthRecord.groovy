package org.github.daroshchanka.agents.launch


import org.github.daroshchanka.LaunchType
import org.github.daroshchanka.Platform
import org.github.daroshchanka.HealthRecord

class LaunchHealthRecord implements HealthRecord {

  Metadata metadata
  Calculations calculations

  static class Metadata {
    String project
    String launchName
    LaunchType launchType
    Platform platform
  }

  static class Calculations {
    BigDecimal reliability
    BigDecimal duration
    Integer testsCount
    BigDecimal launchFrequency
    BigDecimal health
  }

}
