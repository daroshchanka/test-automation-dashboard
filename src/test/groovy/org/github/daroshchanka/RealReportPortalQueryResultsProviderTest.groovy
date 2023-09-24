package org.github.daroshchanka

import groovy.util.logging.Log4j2
import org.github.daroshchanka.infrastructure.db.launch.LaunchHealthTable
import org.github.daroshchanka.agents.launch.LaunchHealthAgent
import org.github.daroshchanka.agents.launch.LaunchQueryCommand
import org.github.daroshchanka.agents.launch.providers.ReportPortalQueryResultsProvider
import org.github.daroshchanka.agents.launch.strategies.DefaultLaunchHealthCalculationStrategy
import org.testng.annotations.Test

@Log4j2
class RealReportPortalQueryResultsProviderTest {

  static String project = 'default_personal'
  static String token = 'PROVIDE_TOKEN'

  @Test
  void test() {
    new LaunchHealthTable().init()
    def strategy = new DefaultLaunchHealthCalculationStrategy(
        reliabilityGrades: new InspectionGrades(95, 85),
        frequencyGrades: new InspectionGrades(36, 72),
        durationGrades: new InspectionGrades(25, 60),
        scanDepthDays: 14,
    )
    ReportPortalQueryResultsProvider provider = new ReportPortalQueryResultsProvider(
        'https://demo.reportportal.io/', token
    )
    def command = new LaunchQueryCommand().tap {
      project = this.project
      launchName = 'Demo Api Tests'
      launchType = LaunchType.INTEGRATION
      platform = Platform.API
      daysHistoricalInterval = strategy.scanDepthDays
    }

    25.times { t ->
      new LaunchHealthAgent(strategy, provider).scanHealth(command)
    }
  }

}
