package org.github.daroshchanka.agents.launch.providers

import org.github.daroshchanka.agents.launch.ILaunchesQueryResultsProvider
import org.github.daroshchanka.agents.launch.LaunchQueryCommand
import groovy.json.JsonSlurper
import org.github.daroshchanka.agents.launch.LaunchesQueryResult
import org.apache.http.HttpResponse
import org.apache.http.client.fluent.Request
import org.apache.http.client.fluent.Response
import org.apache.http.client.utils.URIBuilder
import org.apache.http.util.EntityUtils

import static java.time.LocalDateTime.now
import static org.apache.groovy.datetime.extensions.DateTimeExtensions.toCalendar

class ReportPortalQueryResultsProvider implements ILaunchesQueryResultsProvider {

  private String baseUrl
  private String token

  ReportPortalQueryResultsProvider(String baseUrl, String token) {
    this.baseUrl = baseUrl
    this.token = token
  }

  @Override
  LaunchesQueryResult getResult(LaunchQueryCommand command) {
    long historyDepthScanLimit = toCalendar(now().minusDays(command.daysHistoricalInterval)).timeInMillis
    Map responseData = doGetLaunchesRequest(command.project, command.launchName, 1)
    int totalPages = responseData?.page?.totalPages
    List<Map> allLaunches = responseData.content as List<Map>

    if (totalPages > 1) {
      int currentPage = 1
      while (allLaunches.every { it?.endTime > historyDepthScanLimit } && currentPage < totalPages) {
        currentPage++
        allLaunches.addAll(doGetLaunchesRequest(command.project, command.launchName, currentPage).content as List)
      }
    }
    List<Map> analyzedAndBelongToQueryTimeIntervalLaunches = allLaunches.findAll {
      it.endTime && it.statistics?.executions?.total > 0
    }.findAll {
      it.statistics?.defects?.to_investigate == null && it?.endTime > historyDepthScanLimit
    }

    List<LaunchesQueryResult.LaunchData> launches = analyzedAndBelongToQueryTimeIntervalLaunches
        .collect { Map rpLaunch ->
          new LaunchesQueryResult.LaunchData().tap {
            startTime = rpLaunch?.startTime as long
            endTime = rpLaunch?.endTime as long
            totalTests = (rpLaunch.statistics?.executions?.total ?: 0) as int
            passedTests = (rpLaunch.statistics?.executions?.passed ?: 0) as int
            productBugTests = (rpLaunch.statistics?.defects?.product_bug?.total ?: 0) as int
          }
        }.sort { it.endTime }.reverse()

    new LaunchesQueryResult(launches: launches)
  }

  private Map doGetLaunchesRequest(String project, String name, int page) {
    URIBuilder uriBuilder = new URIBuilder("$baseUrl/api/v1/$project/launch")
        .addParameter('page.size', 100.toString())
        .addParameter('page.sort', 'asc')
        .addParameter('page.page', page.toString())
        .addParameter('filter.eq.name', name)
        .addParameter('access_token', token)
    Response response = Request.Get(uriBuilder.build()).connectTimeout(1000).socketTimeout(1000).execute()
    HttpResponse httpResponse = response.returnResponse()
    if (httpResponse.getStatusLine().statusCode != 200) {
      return [
          page   : [totalPages: 0],
          content: []
      ]
    }
    new JsonSlurper().parseText(EntityUtils.toString(httpResponse.getEntity())) as Map
  }
}
