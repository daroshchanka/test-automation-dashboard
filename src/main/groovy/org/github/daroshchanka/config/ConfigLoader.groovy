package org.github.daroshchanka.config

import groovy.util.logging.Log4j2
import org.yaml.snakeyaml.Yaml

@Log4j2
class ConfigLoader {

  static List<Map> getProjectConfigs() {
    (loadGlobalConfig().projects as List<String>).collect {
      new Yaml().load(ConfigLoader.getClassLoader().getResourceAsStream(it)) as Map
    }
  }

  static Map loadGlobalConfig() {
    new Yaml().load(ConfigLoader.getClassLoader().getResourceAsStream('config.yaml'))
  }

  static Integer getScanDepthDaysInterval(Map projectConfig, Map globalConfig) {
    def forProject = projectConfig?['scan-depth-days'] as Integer
    if (forProject) {
      log.debug("Taken scan-depth-days defined on PROJECT level: $forProject")
      return forProject
    }
    def global = globalConfig?['scan-depth-days'] as Integer
    if (global) {
      log.debug("Taken scan-depth-days defined on GLOBAL level: $global")
      return global
    }
    throw new IllegalStateException("Cannot determine scan-depth-days for projectConfig:$projectConfig")
  }

  static void main(args) {
    getProjectConfigs()
  }
}
