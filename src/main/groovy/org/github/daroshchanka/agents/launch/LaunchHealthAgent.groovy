package org.github.daroshchanka.agents.launch

import groovy.util.logging.Log4j2
import org.github.daroshchanka.agents.IHealthAgent
import org.github.daroshchanka.agents.IQueryResultsProvider
import org.github.daroshchanka.infrastructure.db.launch.LaunchHealthTable

@Log4j2
class LaunchHealthAgent implements IHealthAgent<LaunchQueryCommand, LaunchHealthRecord> {

  private ILaunchHealthCalculationStrategy healthCalculationStrategy
  private IQueryResultsProvider<LaunchesQueryResult, LaunchQueryCommand> resultsProvider

  LaunchHealthAgent(
      ILaunchHealthCalculationStrategy healthCalculationStrategy,
      IQueryResultsProvider<LaunchesQueryResult, LaunchQueryCommand> resultsProvider
  ) {
    this.healthCalculationStrategy = healthCalculationStrategy
    this.resultsProvider = resultsProvider
  }

  @Override
  LaunchHealthRecord query(LaunchQueryCommand command) {
    healthCalculationStrategy.calculate(
        command.getLaunchMetadata(),
        resultsProvider.getResult(command) as LaunchesQueryResult
    )
  }

  @Override
  void store(LaunchHealthRecord healthRecord) {
    new LaunchHealthTable().insert(healthRecord)
    log.info(healthRecord.toString())
  }

}
