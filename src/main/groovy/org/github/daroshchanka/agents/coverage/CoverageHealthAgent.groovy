package org.github.daroshchanka.agents.coverage

import groovy.util.logging.Log4j2
import org.github.daroshchanka.agents.IHealthAgent
import org.github.daroshchanka.infrastructure.db.coverage.CoverageHealthTable

@Log4j2
class CoverageHealthAgent implements IHealthAgent<CoverageQueryCommand, CoverageHealthRecord> {

  private ICoverageHealthCalculationStrategy healthCalculationStrategy
  private ICoverageQueryResultsProvider resultsProvider

  CoverageHealthAgent(
      ICoverageHealthCalculationStrategy healthCalculationStrategy,
      ICoverageQueryResultsProvider resultsProvider
  ) {
    this.healthCalculationStrategy = healthCalculationStrategy
    this.resultsProvider = resultsProvider
  }

  @Override
  CoverageHealthRecord query(CoverageQueryCommand command) {
    healthCalculationStrategy.calculate(
        command.getCoverageMetadata(),
        resultsProvider.getResult(command)
    )
  }

  @Override
  void store(CoverageHealthRecord healthRecord) {
    new CoverageHealthTable().insert(healthRecord)
    log.info(healthRecord.toString())
  }

}
