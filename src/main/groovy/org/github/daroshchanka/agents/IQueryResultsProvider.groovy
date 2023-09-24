package org.github.daroshchanka.agents

interface IQueryResultsProvider<R extends IQueryResult, C extends IQueryCommand> {

  R getResult(C command)

}
