package org.github.daroshchanka.config

import groovy.util.logging.Log4j2
import org.atteo.evo.inflector.English

@Log4j2
abstract class ConfigConsumer {

  protected Map globalConfig
  protected Map projectConfig
  protected Map itemConfig
  protected String mainKey
  protected String itemName

  ConfigConsumer(Map itemConfig, Map projectConfig, Map globalConfig) {
    this.itemConfig = itemConfig
    this.projectConfig = projectConfig
    this.globalConfig = globalConfig
  }

  protected getConfig(SubConfigType configType, String type, String... keys) {
    def configTypeName = configType.getKey()
    def forLaunch = extractProperties(itemConfig?[configTypeName] as Map, keys)
    if (forLaunch) {
      log.debug("[$mainKey][$keys] defined on ${itemName.toUpperCase()} level: $forLaunch")
      return forLaunch
    }
    def projectData = (projectConfig[mainKey]?[translateKeyNameToPlural(configTypeName)] as List)?.with { l->
      type ? l.find { i-> i?['type'] == type } : first()
    } as Map
    def forProject = extractProperties(projectData, keys)
    if (forProject) {
      log.debug("[$mainKey][$keys] defined on PROJECT level: $forProject")
      return forProject
    }
    def globalData = (globalConfig[mainKey]?[translateKeyNameToPlural(configTypeName)] as List)?.with { l->
      type ? l.find { i-> i?['type'] == type } : first()
    } as Map
    def global = extractProperties(globalData, keys)
    if (global) {
      log.debug("[$mainKey][$keys] defined on GLOBAL level: $global")
      return global
    }
    throw new IllegalStateException("Cannot determine [$mainKey][$keys] for ${itemName}Config:$itemConfig, projectConfig:$projectConfig")
  }

  private static extractProperties(Map source, String... keys) {
    def result = source
    keys.each {
      result = result?[it]
    }
    result
  }

  private static String translateKeyNameToPlural(String key) {
    English.plural(key)
  }

}
