package org.github.daroshchanka.config.launch

import groovy.transform.InheritConstructors
import org.github.daroshchanka.LaunchType
import org.github.daroshchanka.Platform
import org.github.daroshchanka.agents.launch.LaunchQueryCommand
import org.github.daroshchanka.config.ConfigLoader

@InheritConstructors
class LaunchQueryCommandBuilder extends LaunchConfigConsumer {

  LaunchQueryCommand build() {
    new LaunchQueryCommand().tap {
      project = projectConfig.name as String
      launchName = itemConfig.name
      launchType = LaunchType[itemConfig.type as String] as LaunchType
      platform = Platform[itemConfig.platform as String] as Platform
      daysHistoricalInterval = ConfigLoader.getScanDepthDaysInterval(projectConfig, globalConfig)
    }
  }
}
