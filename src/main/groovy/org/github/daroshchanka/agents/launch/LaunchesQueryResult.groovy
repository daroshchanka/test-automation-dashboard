package org.github.daroshchanka.agents.launch

import groovy.transform.Canonical
import groovy.transform.ToString
import org.github.daroshchanka.agents.IQueryResult

@ToString
@Canonical
class LaunchesQueryResult implements IQueryResult {

  List<LaunchData> launches

  @ToString
  static class LaunchData {
    long startTime
    long endTime
    int totalTests
    int passedTests
    int productBugTests
  }
}
