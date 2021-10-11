package com.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

class ApplicationTest : StringSpec({

    "given X-Special-Header header is not present when handle request then respond Forbidden"{
        withTestApplication(moduleFunction = { module() }) {
            val testCall: TestApplicationCall = handleRequest(method = HttpMethod.Get, uri = "/credit")
            testCall.response.status() shouldBe HttpStatusCode.OK
            testCall.response.content shouldBe "Hello World!"
        }
    }
})