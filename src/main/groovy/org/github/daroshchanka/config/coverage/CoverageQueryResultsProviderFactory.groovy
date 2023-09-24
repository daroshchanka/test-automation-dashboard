package org.github.daroshchanka.config.coverage

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.agents.coverage.ICoverageQueryResultsProvider
import org.github.daroshchanka.config.SubConfigType

@Log4j2
@InheritConstructors
class CoverageQueryResultsProviderFactory extends CoverageConfigConsumer {

  ICoverageQueryResultsProvider getProvider() {
    def provider
    def type = getConfig(SubConfigType.PROVIDER, null, 'type')
    switch (type) {
      case 'test-rail':
        provider = new TestRailQueryResultsProviderBuilder(itemConfig, projectConfig, globalConfig).build()
        break
      default:
        throw new IllegalStateException("$type provider not supported")
    }
    provider
  }

}
