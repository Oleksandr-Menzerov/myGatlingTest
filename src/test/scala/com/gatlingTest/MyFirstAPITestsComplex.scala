package com.gatlingTest

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class MyFirstAPITestsComplex extends Simulation {

	val httpProtocol = http
		.baseUrl("https://localhost:7069/api/rockers")
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("uk,ru;q=0.9,en-GB;q=0.8,en-US;q=0.7,en;q=0.6")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
		.headers(Map("Content-Type" -> "application/json"))

	val scn = scenario("MyFirstAPITests")
		.exec(http(session => "LoadAllRockers")
			.get("")
			.check(status is 200)
		)
		.pause(2)
		.exec(http(session => "AddNewRocker")
			.post("")
			.body(StringBody("""{
                           "name": "Bruce Dickinson"
                       } """)).asJson
			.check(status is 201)
			.check(
				jsonPath("$.id").saveAs("rockerId")
			))
		.pause(2)
		.exec(http(session => "Update rocker")
			.put("/${rockerId}")
			.body(StringBody("""{
                           "id": ${rockerId},
                           "name": "Bruce Dickinson",
                           "band": "Iron Maiden"
                       } """)).asJson
			.check(status is 204))
		.pause(2)
		.exec(http(session => "Delete rocker")
			.delete("/${rockerId}")
			.check(status is 204))

	setUp(
		scn.inject(
			nothingFor(4), // 1
			atOnceUsers(10), // 2
			rampUsers(10).during(5), // 3
			constantUsersPerSec(20).during(15), // 4
			constantUsersPerSec(20).during(15).randomized, // 5
			rampUsersPerSec(10).to(20).during(1.minutes), // 6
			rampUsersPerSec(10).to(20).during(1.minutes).randomized, // 7
		).protocols(httpProtocol)
	)
}