package org.github.daroshchanka.agents.coverage.providers

import groovy.json.JsonSlurper
import org.github.daroshchanka.agents.coverage.CoverageQueryCommand
import org.github.daroshchanka.agents.coverage.CoverageQueryResult
import org.github.daroshchanka.agents.coverage.ICoverageQueryResultsProvider
import org.apache.commons.codec.binary.Base64
import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.client.fluent.Request
import org.apache.http.client.fluent.Response
import org.apache.http.message.BasicHeader
import org.apache.http.util.EntityUtils

class TestRailQueryResultsProvider implements ICoverageQueryResultsProvider {

  private String baseUrl
  private String username
  private String token
  private String appName
  private Header[] headers

  TestRailQueryResultsProvider(String baseUrl, String username, String token, String appName) {
    this.baseUrl = baseUrl
    this.username = username
    this.token = token
    this.appName = appName
    byte[] encoded = Base64.encodeBase64("${username}:${token}".getBytes('UTF-8'))
    headers = [
        new BasicHeader('Content-Type', 'application/json'),
        new BasicHeader('Authorization', "Basic ${new String(encoded)}")
    ]
    if (appName) {
      headers << new BasicHeader('User-Agent', appName)
    }
  }

  @Override
  CoverageQueryResult getResult(CoverageQueryCommand command) {
    List<Map> allCases = []
    command.suiteIds.each {
      allCases += getCases(command.projectId, it)
    }
    allCases = allCases.findAll {
      it?.is_deleted != 1
    }
    new CoverageQueryResult(
        new CoverageQueryResult.Coverage(automated: 0, possible: 0, total: 0),
        new CoverageQueryResult.Coverage(automated: 0, possible: 0, total: 0),
    )
  }

  private List getCases(String projectId, String suiteId) {
    List cases = []
    def endpoint = "${baseUrl}index.php?"
    def url = "${endpoint}/api/v2/get_cases/$projectId&limit=250${suiteId ? "&suite_id=$suiteId" : ''}"
    def firstPageData = doGet(url)
    cases.addAll(firstPageData['cases'])
    def nextUrl = firstPageData?['_links']?['next']
    while (nextUrl != null) {
      def nextPageData =  doGet("$endpoint$nextUrl")
      cases.addAll(nextPageData['cases'])
      nextUrl = nextPageData?['_links']?['next']
    }
    cases
  }

  private Map doGet(String url) {
    Response response = Request.Get(url).connectTimeout(1000).socketTimeout(1000).setHeaders(headers).execute()
    HttpResponse httpResponse = response.returnResponse()
    if (httpResponse.getStatusLine().statusCode != 200) {
      return [cases: []]
    }
    new JsonSlurper().parseText(EntityUtils.toString(httpResponse.getEntity())) as Map
  }
}
