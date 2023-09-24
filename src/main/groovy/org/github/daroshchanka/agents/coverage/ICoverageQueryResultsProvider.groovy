package org.github.daroshchanka.agents.coverage


import org.github.daroshchanka.agents.IQueryResultsProvider

interface ICoverageQueryResultsProvider extends IQueryResultsProvider<CoverageQueryResult, CoverageQueryCommand> {

  CoverageQueryResult getResult(CoverageQueryCommand command)

}
