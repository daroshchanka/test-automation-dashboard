package org.github.daroshchanka.config.coverage

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.agents.coverage.ICoverageHealthCalculationStrategy
import org.github.daroshchanka.config.SubConfigType

@Log4j2
@InheritConstructors
class CoverageHealthCalculationStrategyFactory extends CoverageConfigConsumer {

  ICoverageHealthCalculationStrategy getStrategy() {
    def strategy
    def type = getConfig(SubConfigType.STRATEGY, null, 'type')
    switch (type) {
      case 'default':
        strategy = new DefaultCoverageHealthCalculationStrategyBuilder(itemConfig, projectConfig, globalConfig).build()
        break
      default:
        throw new IllegalStateException("$type strategy not supported")
    }
    strategy
  }

}
