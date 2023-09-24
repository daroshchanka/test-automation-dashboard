package org.github.daroshchanka.config.launch

import groovy.transform.InheritConstructors
import org.github.daroshchanka.agents.launch.ILaunchHealthCalculationStrategy
import org.github.daroshchanka.agents.launch.ILaunchesQueryResultsProvider
import org.github.daroshchanka.agents.launch.LaunchQueryCommand

@InheritConstructors
class LaunchHealthConfigResolver extends LaunchConfigConsumer {

  ILaunchHealthCalculationStrategy getStrategy() {
    new LaunchHealthCalculationStrategyFactory(itemConfig, projectConfig, globalConfig).getStrategy()
  }

  LaunchQueryCommand getCommand() {
    new LaunchQueryCommandBuilder(itemConfig, projectConfig, globalConfig).build()
  }

  ILaunchesQueryResultsProvider getProvider() {
    new LaunchesQueryResultsProviderFactory(itemConfig, projectConfig, globalConfig).getProvider()
  }

}
