package org.github.daroshchanka.agents.coverage

import org.github.daroshchanka.Platform
import org.github.daroshchanka.agents.IQueryCommand

class CoverageQueryCommand implements IQueryCommand {

  String projectId
  List<String> suiteIds
  Platform platform

  CoverageHealthRecord.Metadata getCoverageMetadata() {
    new CoverageHealthRecord.Metadata(
        platform: platform
    )
  }

}
