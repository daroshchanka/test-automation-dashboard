package org.github.daroshchanka.agents.launch.strategies

import org.github.daroshchanka.InspectionGrades
import org.github.daroshchanka.InspectionStatus
import org.github.daroshchanka.agents.launch.ILaunchHealthCalculationStrategy
import org.github.daroshchanka.agents.launch.LaunchesQueryResult
import org.github.daroshchanka.agents.launch.LaunchHealthRecord

class DefaultLaunchHealthCalculationStrategy implements ILaunchHealthCalculationStrategy {

  int scanDepthDays
  private InspectionGrades<BigDecimal> reliabilityGrades
  private InspectionGrades<BigDecimal> frequencyGrades
  private InspectionGrades<BigDecimal> durationGrades

  @Override
  LaunchHealthRecord calculate(LaunchHealthRecord.Metadata metadata, LaunchesQueryResult result) {
    new LaunchHealthRecord(
        metadata: metadata,
        calculations: getCalculations(result.getLaunches())
    )
  }

  private LaunchHealthRecord.Calculations getCalculations(List<LaunchesQueryResult.LaunchData> launches) {
    new LaunchHealthRecord.Calculations(
        testsCount: retrieveTestsCount(launches),
        reliability: retrieveReliability(launches),
        duration: retrieveDuration(launches),
        launchFrequency: retrieveLaunchFrequency(launches, scanDepthDays),
    ).tap {
      health = calculateHealth(it)
    }
  }

  private static int retrieveTestsCount(List<LaunchesQueryResult.LaunchData> data) {
    data.sort { it.startTime }.last().totalTests
  }

  private static BigDecimal retrieveReliability(List<LaunchesQueryResult.LaunchData> data) {
    data.collect { (it.passedTests + it.productBugTests) * 100 / it.totalTests }.average() as BigDecimal
  }

  private static BigDecimal retrieveDuration(List<LaunchesQueryResult.LaunchData> data) {
    data.collect { ((it.endTime - it.startTime) / (1000 * 60)) as BigDecimal }.average() as BigDecimal
  }

  private static BigDecimal retrieveLaunchFrequency(List<LaunchesQueryResult.LaunchData> data, int scanDepthDays) {
    BigDecimal workingDaysAWeekRatio = 5 / 7
    BigDecimal linearFrequency = scanDepthDays * 24 * workingDaysAWeekRatio / data.size()
    BigDecimal timeFromLastLaunch =
        (Calendar.instance.timeInMillis - data.sort { it.startTime }.last().startTime) *
            workingDaysAWeekRatio / (3600 * 1000)
    timeFromLastLaunch > linearFrequency ? timeFromLastLaunch : linearFrequency
  }

  /**
   * The lower InspectionStatus value, the less health it brings
   * */
  private static BigDecimal getInspectionStatusRatio(InspectionStatus status) {
    status == InspectionStatus.HIGH ? 1
        : status == InspectionStatus.MEDIUM ? 0.9
        : 0.75
  }

  private BigDecimal calculateHealth(LaunchHealthRecord.Calculations calculations) {
    BigDecimal reliabilityHealth = calculations.reliability.with {
      getInspectionStatusRatio(reliabilityGrades.evaluate(it)) * it / 100 * 0.4
    }
    BigDecimal frequencyHealth = calculations.launchFrequency.with {
      def ratio = it <= frequencyGrades.forHigh ? 1 : frequencyGrades.forHigh / it
      getInspectionStatusRatio(frequencyGrades.evaluate(it)) * ratio * 0.4
    }
    BigDecimal durationHealth = calculations.duration.with {
      def ratio = it <= durationGrades.forHigh ? 1 : durationGrades.forHigh / it
      getInspectionStatusRatio(durationGrades.evaluate(it)) * ratio * 0.2
    }
    (reliabilityHealth + frequencyHealth + durationHealth) * 100
  }

}
