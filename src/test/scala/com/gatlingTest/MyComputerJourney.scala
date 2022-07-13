package com.gatlingTest

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MyComputerJourney extends Simulation {

	val httpProtocol = http
		.baseUrl("https://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("uk,ru;q=0.9,en-GB;q=0.8,en-US;q=0.7,en;q=0.6")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")

	val scn = scenario("MyComputerJourney")
		.exec(http("LoadHomePage")
			.get("/computers"))
		.pause(5)
		.exec(http("LoadNewComputerPage")
			.get("/computers/new"))
		.pause(5)
		.exec(http("CreateNewComputerPage")
			.post("/computers")
			.formParam("name", "Olympic-S")
			.formParam("introduced", "1991-01-01")
			.formParam("discontinued", "1995-05-05")
			.formParam("company", "24"))
		.pause(5)
		.exec(http("FilterComputer")
			.get("/computers?f=Olympic-S"))
		.pause(5)
		.exec(http("FilterComputer_2")
			.get("/computers?f=ACE"))
		.pause(5)
		.exec(http("GetSingleComputer")
			.get("/computers/381"))
		.pause(5)
		.exec(http("DeleteComputer")
			.post("/computers/381/delete"))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}