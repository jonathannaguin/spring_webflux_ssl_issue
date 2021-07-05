package load

import java.util.concurrent.ThreadLocalRandom
import java.time._
import java.time.format.DateTimeFormatter

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import java.util.UUID.randomUUID
import scala.language.postfixOps
import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer
import io.gatling.core.controller.inject.open.OpenInjectionStep


class MySimulation extends Simulation {
    val SERVICE_BASE_URL: String = sys.env.getOrElse("URL", "https://localhost:8081")
    val REQUEST_PER_SECOND: Double = sys.env.getOrElse("REQUEST_PER_SECOND", "100").toDouble
    val RUN_FOR: Double = sys.env.getOrElse("RUN_FOR", "15").toDouble

    val httpProtocol: HttpProtocolBuilder = http.baseUrl(SERVICE_BASE_URL).shareConnections

    val RESPONSE_CODE_KEY = "RESPONSE_CODE_KEY"
    val LOOP_COUNTER_KEY = "LOOP_COUNTER_KEY"
    val GLOBAL_COUNT_START : Long = 447599000000L
    val globalCount = new java.util.concurrent.atomic.AtomicLong(GLOBAL_COUNT_START)
    val globalUniqueNumFeeder: Iterator[Map[String, Long]] = Iterator.continually(Map("uniqueID" -> globalCount.getAndIncrement()))
    val random: ThreadLocalRandom = ThreadLocalRandom.current()

    def printScenarioParameters(): Unit = {
        println("URL -> " + SERVICE_BASE_URL)
        println("Target requests per second -> " + REQUEST_PER_SECOND)
        println("Simulation duration (min) -> " + RUN_FOR)
    }

    def incrementGlobalUniqueNumChain: ChainBuilder = {
        exec {
          feed(globalUniqueNumFeeder)
        }
    }

    def getUniqueCountFromSession(session: Session): String = {
        session("uniqueID").as[String]
    }

    def doRequest(): ChainBuilder = {
        exec(session => session.set("request", getRequest(session)))
          .exec(sendRequest)
    }

    def getRequest(session: Session): String = {

    def id = "load-test-" + randomUUID().toString

    val request =
          s"""{
             |  "fieldA": "stuff",
             |  "fieldB": "id"
             |}""".stripMargin
        request
    }

    def sendRequest: ChainBuilder = {
      val now = DateTimeFormatter.
          ofPattern("dd-MMM-yyyy HH:mm:ss z").
          format(Instant.now.atZone(ZoneId.of("GMT")))
      val headers: Map[String, String] = Map("Content-Type" -> "application/json", "Accept" -> "*/*", "Date" -> now, "RequestId" -> randomUUID().toString)
      exec(http("Request")
        .post("/demo/request")
        .headers(headers)
        .body(StringBody(session => session("request").as[String]))
        .check(status.is(200)))
    }

    val scenarioNoRetry: ScenarioBuilder = scenario("Test")
        .exec(incrementGlobalUniqueNumChain)
        .exec(doRequest())

    printScenarioParameters()

    var stairs = ListBuffer[Int]()
    var done = false;
    var target = REQUEST_PER_SECOND;
    var stair = 0.0

    while (!done) {
      stair = target
      if (target <= 200){
        stair = Math.max(0, Math.min(target, 50))
        done = true
      }
      stairs += (stair.toInt)
      target = (target - 500)
    }

    var number_stairs = stairs.length - 1
    var increment = REQUEST_PER_SECOND/number_stairs
    if (number_stairs == 0) {
        increment = REQUEST_PER_SECOND
    }
    println("Number of steps -> " + number_stairs)
    println("Increment in each stair -> " + increment)
    println("Starting from -> " + stairs(number_stairs))

    setUp(
        scenarioNoRetry.inject(
            incrementUsersPerSec(increment)
                .times(number_stairs)
                .eachLevelLasting(3.minutes)
                .separatedByRampsLasting(30.seconds)
                .startingFrom(stairs(number_stairs)),
            constantUsersPerSec(REQUEST_PER_SECOND) during RUN_FOR.minutes
        ).protocols(httpProtocol)
    )
}
