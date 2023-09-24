package org.github.daroshchanka.agents.coverage

interface ICoverageHealthCalculationStrategy {

  CoverageHealthRecord calculate(CoverageHealthRecord.Metadata metadata, CoverageQueryResult result)

}
