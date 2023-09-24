package org.github.daroshchanka.config.coverage

import groovy.transform.InheritConstructors
import org.github.daroshchanka.agents.coverage.CoverageQueryCommand
import org.github.daroshchanka.agents.coverage.ICoverageHealthCalculationStrategy
import org.github.daroshchanka.agents.coverage.ICoverageQueryResultsProvider

@InheritConstructors
class CoverageHealthConfigResolver extends CoverageConfigConsumer {

  ICoverageHealthCalculationStrategy getStrategy() {
    new CoverageHealthCalculationStrategyFactory(itemConfig, projectConfig, globalConfig).getStrategy()
  }

  CoverageQueryCommand getCommand() {
    new CoverageQueryCommandBuilder(itemConfig, projectConfig, globalConfig).build()
  }

  ICoverageQueryResultsProvider getProvider() {
    new CoverageQueryResultsProviderFactory(itemConfig, projectConfig, globalConfig).getProvider()
  }

}
