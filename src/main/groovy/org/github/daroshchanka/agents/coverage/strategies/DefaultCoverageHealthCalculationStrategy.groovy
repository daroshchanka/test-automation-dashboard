package org.github.daroshchanka.agents.coverage.strategies

import org.github.daroshchanka.InspectionGrades
import org.github.daroshchanka.InspectionStatus
import org.github.daroshchanka.agents.coverage.CoverageQueryResult
import org.github.daroshchanka.agents.coverage.ICoverageHealthCalculationStrategy
import org.github.daroshchanka.agents.coverage.CoverageHealthRecord

class DefaultCoverageHealthCalculationStrategy implements ICoverageHealthCalculationStrategy {

  private InspectionGrades<BigDecimal> overallGrades
  private InspectionGrades<BigDecimal> byCriticalGrades

  CoverageHealthRecord calculate(CoverageHealthRecord.Metadata metadata, CoverageQueryResult result) {
    new CoverageHealthRecord(
        metadata: metadata,
        overall: calculateCoverage(result.getOverall()),
        byCritical: calculateCoverage(result.getByCritical()),
    ).tap {
      health = calculateHealth(overall.coverage, byCritical.coverage)
    }
  }

  private static CoverageHealthRecord.Coverage calculateCoverage(CoverageQueryResult.Coverage result) {
    new CoverageHealthRecord.Coverage(
        automated: result.automated,
        possible: result.possible,
        total: result.total,
    ).tap {
      coverage = total == 0 ? 0 : (automated / total) * 100
    }
  }

  private BigDecimal calculateHealth(BigDecimal overall, BigDecimal byCritical) {
    (overall * getInspectionStatusRatio(overallGrades.evaluate(overall)) * 0.45) +
        (byCritical * getInspectionStatusRatio(byCriticalGrades.evaluate(byCritical)) * 0.55)
  }

  /**
   * The lower InspectionStatus value, the less health it brings
   * */
  private static BigDecimal getInspectionStatusRatio(InspectionStatus status) {
    status == InspectionStatus.HIGH ? 1
        : status == InspectionStatus.MEDIUM ? 0.9
        : 0.75
  }
}
