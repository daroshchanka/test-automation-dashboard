package org.github.daroshchanka.config.launch

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.agents.launch.ILaunchesQueryResultsProvider
import org.github.daroshchanka.config.SubConfigType

@Log4j2
@InheritConstructors
class LaunchesQueryResultsProviderFactory extends LaunchConfigConsumer {

  ILaunchesQueryResultsProvider getProvider() {
    def provider
    def type = getConfig(SubConfigType.PROVIDER, null, 'type')
    switch (type) {
      case 'report-portal':
        provider = new ReportPortalQueryResultsProviderBuilder(itemConfig, projectConfig, globalConfig).build()
        break
      default:
        throw new IllegalStateException("$type provider not supported")
    }
    provider
  }
}
