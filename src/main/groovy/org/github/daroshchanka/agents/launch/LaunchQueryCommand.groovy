package org.github.daroshchanka.agents.launch


import org.github.daroshchanka.agents.IQueryCommand
import org.github.daroshchanka.LaunchType
import org.github.daroshchanka.Platform

class LaunchQueryCommand implements IQueryCommand {

  String project
  String launchName
  LaunchType launchType
  Platform platform
  int daysHistoricalInterval

  LaunchHealthRecord.Metadata getLaunchMetadata() {
    new LaunchHealthRecord.Metadata(
        project: project,
        launchName: launchName,
        launchType: launchType,
        platform: platform
    )
  }

}
