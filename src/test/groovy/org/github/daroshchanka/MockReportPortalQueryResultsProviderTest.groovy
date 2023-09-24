package org.github.daroshchanka

import groovy.json.JsonOutput
import groovy.util.logging.Log4j2
import org.github.daroshchanka.config.ConfigLoader
import org.github.daroshchanka.config.launch.LaunchHealthConfigResolver
import org.github.daroshchanka.infrastructure.db.launch.LaunchHealthTable
import org.github.daroshchanka.agents.launch.LaunchHealthAgent
import org.mockserver.integration.ClientAndServer
import org.mockserver.mock.action.ExpectationResponseCallback
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import static java.time.LocalDateTime.now
import static org.apache.groovy.datetime.extensions.DateTimeExtensions.toCalendar
import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

@Log4j2
class MockReportPortalQueryResultsProviderTest {

  static ClientAndServer mockServer
  static String project1 = 'project-a'
  static String project2 = 'project-b'
  static String project3 = 'project-c'
  static String project4 = 'project-d'

  @BeforeClass
  void setup() {
    new LaunchHealthTable().init()
    setupMockServer()
  }

  @Test
  void test() {
    Map globalConfig = ConfigLoader.loadGlobalConfig()
    ConfigLoader.getProjectConfigs().each { Map projectConfig ->
      projectConfig.launches.each { Map launchConfig ->
        def configResolver = new LaunchHealthConfigResolver(launchConfig, projectConfig, globalConfig)
        def strategy = configResolver.getStrategy()
        def command = configResolver.getCommand()
        def provider = configResolver.getProvider()
        20.times { t ->
          new LaunchHealthAgent(strategy, provider).scanHealth(command)
        }
      }
    }

  }

  @AfterClass
  void stopMockServer() {
    mockServer.stop()
  }

  static setupMockServer() {
    mockServer = startClientAndServer(1080)

    mockServer.when(
        request()
            .withMethod('GET')
            .withPath("/report-portal/api/v1/$project1/launch")
    ).respond(
        new ExpectationResponseCallback() {
          @Override
          HttpResponse handle(HttpRequest httpRequest) {
            response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(JsonOutput.toJson(staticData(
                    httpRequest.getQueryStringParameterList()
                        .find { it.name.getValue() == 'page.page' }.values.first().getValue() as int)
                ))
          }
        }
    )
    mockServer.when(
        request()
            .withMethod('GET')
            .withPath("/report-portal/api/v1/$project2/launch")
    ).respond(
        new ExpectationResponseCallback() {
          @Override
          HttpResponse handle(HttpRequest httpRequest) {
            response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(JsonOutput.toJson(staticData(
                    httpRequest.getQueryStringParameterList()
                        .find { it.name.getValue() == 'page.page' }.values.first().getValue() as int)
                ))
          }
        }
    )
    mockServer.when(
        request()
            .withMethod('GET')
            .withPath("/report-portal/api/v1/$project3/launch")
    ).respond(
        new ExpectationResponseCallback() {
          @Override
          HttpResponse handle(HttpRequest httpRequest) {
            response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(JsonOutput.toJson(staticData(
                    httpRequest.getQueryStringParameterList()
                        .find { it.name.getValue() == 'page.page' }.values.first().getValue() as int)
                ))
          }
        }
    )
    mockServer.when(
        request()
            .withMethod('GET')
            .withPath("/report-portal/api/v1/$project4/launch")
    ).respond(
        new ExpectationResponseCallback() {
          @Override
          HttpResponse handle(HttpRequest httpRequest) {
            response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(JsonOutput.toJson(staticData(
                    httpRequest.getQueryStringParameterList()
                        .find { it.name.getValue() == 'page.page' }.values.first().getValue() as int)
                ))
          }
        }
    )

  }

  static Map staticData(int page) {
    [
        page   : [
            totalPages: 5,
        ],
        content: [
            generateMockLaunch(page, 70 - page * 2, 140 - page * 4),
            generateMockLaunch(page, 65 - page * 2, 130 - page * 4),
            generateMockLaunch(page, 60 - page * 2, 120 - page * 4),
            generateMockLaunch(page, 70 - page * 2, 140 - page * 4),
            generateMockLaunch(page, 50 - page * 2, 75 - page * 4),
        ]
    ]
  }

  static Map generateMockLaunch(int page, int duration, int minTests) {
    [
        startTime : toCalendar(now().minusDays(page * 3).minusMinutes(duration)).timeInMillis,
        endTime   : toCalendar(now().minusDays(page * 3)).timeInMillis,
        statistics: [
            executions: [
                total : minTests + new Random().nextInt(10),
                passed: minTests + new Random().nextInt(10) - 30,
            ],
            defects   : [
                product_bug: [total: new Random().nextInt(10)],
            ]
        ]
    ]
  }
}
