package org.github.daroshchanka.config.coverage

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.config.ConfigConsumer

@Log4j2
@InheritConstructors
abstract class CoverageConfigConsumer extends ConfigConsumer {

  CoverageConfigConsumer(Map itemConfig, Map projectConfig, Map globalConfig) {
    super(itemConfig, projectConfig, globalConfig)
    mainKey = 'coverage'
    itemName = 'platform'
  }

}
