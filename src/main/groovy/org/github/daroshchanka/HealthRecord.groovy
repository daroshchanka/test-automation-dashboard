package org.github.daroshchanka

import groovy.json.JsonOutput

interface HealthRecord {

  default String toString() {
    JsonOutput.prettyPrint(
        JsonOutput.toJson(this.getProperties().tap { remove('class') })
    )
  }
}