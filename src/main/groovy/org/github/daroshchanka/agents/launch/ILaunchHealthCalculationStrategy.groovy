package org.github.daroshchanka.agents.launch

interface ILaunchHealthCalculationStrategy {

  LaunchHealthRecord calculate(LaunchHealthRecord.Metadata metadata, LaunchesQueryResult result)

}
