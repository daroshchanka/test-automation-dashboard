package org.github.daroshchanka.agents.launch


import org.github.daroshchanka.agents.IQueryResultsProvider

interface ILaunchesQueryResultsProvider extends IQueryResultsProvider<LaunchesQueryResult, LaunchQueryCommand> {

  LaunchesQueryResult getResult(LaunchQueryCommand command)

}
