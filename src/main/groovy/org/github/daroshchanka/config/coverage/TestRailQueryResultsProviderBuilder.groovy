package org.github.daroshchanka.config.coverage

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.config.SubConfigType
import org.github.daroshchanka.agents.coverage.providers.TestRailQueryResultsProvider

@Log4j2
@InheritConstructors
class TestRailQueryResultsProviderBuilder extends CoverageConfigConsumer {

  TestRailQueryResultsProvider build() {
    new TestRailQueryResultsProvider(
        getProviderConfig('base-url'), getProviderConfig('username'), getProviderConfig('token'), getProviderConfig('appName')
    )
  }

  private String getProviderConfig(String key) {
    getConfig(SubConfigType.PROVIDER, 'test-rail', key)
  }
}
