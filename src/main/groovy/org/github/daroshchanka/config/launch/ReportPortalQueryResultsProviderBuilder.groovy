package org.github.daroshchanka.config.launch

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.agents.launch.providers.ReportPortalQueryResultsProvider
import org.github.daroshchanka.config.SubConfigType

@Log4j2
@InheritConstructors
class ReportPortalQueryResultsProviderBuilder extends LaunchConfigConsumer {

  ReportPortalQueryResultsProvider build() {
    new ReportPortalQueryResultsProvider(getProviderConfig('base-url'), getProviderConfig('token'))
  }

  private String getProviderConfig(String key) {
    getConfig(SubConfigType.PROVIDER, 'report-portal', key)
  }


}
