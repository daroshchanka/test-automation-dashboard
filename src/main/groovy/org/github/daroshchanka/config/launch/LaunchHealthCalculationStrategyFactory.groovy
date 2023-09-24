package org.github.daroshchanka.config.launch

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.agents.launch.ILaunchHealthCalculationStrategy
import org.github.daroshchanka.config.SubConfigType

@Log4j2
@InheritConstructors
class LaunchHealthCalculationStrategyFactory extends LaunchConfigConsumer {

  ILaunchHealthCalculationStrategy getStrategy() {
    def strategy
    def type = getConfig(SubConfigType.STRATEGY, null, 'type')
    switch (type) {
      case 'default':
        strategy = new DefaultLaunchHealthCalculationStrategyBuilder(itemConfig, projectConfig, globalConfig).build()
        break
      default:
        throw new IllegalStateException("$type strategy not supported")
    }
    strategy
  }

}
