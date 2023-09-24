package org.github.daroshchanka.config.coverage

import groovy.transform.InheritConstructors
import org.github.daroshchanka.Platform
import org.github.daroshchanka.agents.coverage.CoverageQueryCommand
import org.github.daroshchanka.config.SubConfigType

@InheritConstructors
class CoverageQueryCommandBuilder extends CoverageConfigConsumer {

  CoverageQueryCommand build() {
    new CoverageQueryCommand().tap {
      platform = Platform[itemConfig.values().first()['platform'] as String] as Platform
      String provider = getConfig(SubConfigType.PROVIDER, null, 'type')
      projectId = getConfig(SubConfigType.PROVIDER, provider, 'projectId')
      suiteIds = getConfig(SubConfigType.PROVIDER, provider, 'suiteIds') as List<String>
    }
  }
}
