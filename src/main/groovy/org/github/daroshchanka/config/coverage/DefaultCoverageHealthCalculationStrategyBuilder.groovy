package org.github.daroshchanka.config.coverage

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.InspectionGrades
import org.github.daroshchanka.agents.coverage.strategies.DefaultCoverageHealthCalculationStrategy
import org.github.daroshchanka.config.SubConfigType

@Log4j2
@InheritConstructors
class DefaultCoverageHealthCalculationStrategyBuilder extends CoverageConfigConsumer {

  DefaultCoverageHealthCalculationStrategy build() {
    new DefaultCoverageHealthCalculationStrategy(
        overallGrades: new InspectionGrades(getDefaultStrategyGrades('overall')),
        byCriticalGrades: new InspectionGrades(getDefaultStrategyGrades('byCritical')),
    )
  }

  private List<BigDecimal> getDefaultStrategyGrades(String key) {
    getConfig(SubConfigType.STRATEGY, 'default', 'grades', key) as List<BigDecimal>
  }

}
