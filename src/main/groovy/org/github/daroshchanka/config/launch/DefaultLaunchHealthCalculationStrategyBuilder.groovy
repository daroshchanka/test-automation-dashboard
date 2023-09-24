package org.github.daroshchanka.config.launch

import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j2
import org.github.daroshchanka.InspectionGrades
import org.github.daroshchanka.agents.launch.strategies.DefaultLaunchHealthCalculationStrategy
import org.github.daroshchanka.config.ConfigLoader
import org.github.daroshchanka.config.SubConfigType

@Log4j2
@InheritConstructors
class DefaultLaunchHealthCalculationStrategyBuilder extends LaunchConfigConsumer {

  DefaultLaunchHealthCalculationStrategy build() {
    new DefaultLaunchHealthCalculationStrategy(
        reliabilityGrades: new InspectionGrades(getDefaultStrategyGrades('reliability')),
        frequencyGrades: new InspectionGrades(getDefaultStrategyGrades('frequency')),
        durationGrades: new InspectionGrades(getDefaultStrategyGrades('duration')),
        scanDepthDays: ConfigLoader.getScanDepthDaysInterval(projectConfig, globalConfig),
    )
  }

  private List<BigDecimal> getDefaultStrategyGrades(String key) {
    getConfig(SubConfigType.STRATEGY, 'default', 'grades', key) as List<BigDecimal>
  }

}
