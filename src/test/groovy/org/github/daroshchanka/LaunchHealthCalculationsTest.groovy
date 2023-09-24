package org.github.daroshchanka

import org.github.daroshchanka.agents.IQueryResultsProvider
import org.github.daroshchanka.agents.launch.LaunchesQueryResult
import org.github.daroshchanka.infrastructure.db.launch.LaunchHealthTable
import org.github.daroshchanka.agents.launch.strategies.DefaultLaunchHealthCalculationStrategy
import org.github.daroshchanka.agents.launch.LaunchHealthAgent
import org.github.daroshchanka.agents.launch.LaunchQueryCommand
import org.testng.annotations.Test

import static java.time.LocalDateTime.now
import static org.apache.groovy.datetime.extensions.DateTimeExtensions.toCalendar

class LaunchHealthCalculationsTest {

  @Test
  void rpAgentTest() {
    new LaunchHealthTable().init()
    def strategy = new DefaultLaunchHealthCalculationStrategy(
        reliabilityGrades: new InspectionGrades(95, 85),
        frequencyGrades: new InspectionGrades(36, 72),
        durationGrades: new InspectionGrades(25, 60),
        scanDepthDays: 14,
    )
    def command = new FakeLaunchQueryCommand().tap {
      project = 'project-A'
      launchName = 'web-acceptance'
      launchType = LaunchType.ACCEPTANCE
      platform = Platform.WEB
      daysHistoricalInterval = strategy.scanDepthDays
    }

    100.times { t ->
      new LaunchHealthAgent(strategy, new FakeLaunchesQueryResultsProvider()).scanHealth(
          command.tap { salt = t }
      )
    }
  }

  class FakeLaunchQueryCommand extends LaunchQueryCommand {
    int salt
  }

  class FakeLaunchesQueryResultsProvider implements IQueryResultsProvider<LaunchesQueryResult, FakeLaunchQueryCommand> {
    @Override
    LaunchesQueryResult getResult(FakeLaunchQueryCommand command) {
      new LaunchesQueryResult(
          (1..10).collect {
            new LaunchesQueryResult.LaunchData(
                startTime: toCalendar(now().minusDays(12 - it)).timeInMillis,
                endTime: toCalendar(now().minusDays(12 - it).plusMinutes(25 + command.salt)).timeInMillis,
                totalTests: (15 + command.salt) * 9,
                passedTests: (15 + +command.salt) * 8,
                productBugTests: command.salt
            )
          }
      )
    }
  }
}
