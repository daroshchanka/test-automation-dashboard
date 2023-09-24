package org.github.daroshchanka.agents.coverage

import groovy.transform.Canonical
import groovy.transform.ToString
import org.github.daroshchanka.agents.IQueryResult

@ToString
@Canonical
class CoverageQueryResult implements IQueryResult {

  Coverage overall
  Coverage byCritical

  static class Coverage {
    Integer total
    Integer automated
    Integer possible
  }

}
