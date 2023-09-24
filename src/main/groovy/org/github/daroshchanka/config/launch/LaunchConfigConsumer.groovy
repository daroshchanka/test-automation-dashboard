package org.github.daroshchanka.config.launch

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.config.ConfigConsumer

@Log4j2
@InheritConstructors
abstract class LaunchConfigConsumer extends ConfigConsumer {

  LaunchConfigConsumer(Map itemConfig, Map projectConfig, Map globalConfig) {
    super(itemConfig, projectConfig, globalConfig)
    mainKey = 'launch'
    itemName = 'launch'
  }

}
