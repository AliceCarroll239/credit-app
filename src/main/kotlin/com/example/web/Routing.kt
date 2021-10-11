package com.example.web

import com.example.model.CreditRequest
import com.example.model.ValidationErrors
import com.example.service.CreditService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.credit() {
    val service by inject<CreditService>()

    route("/credit") {
        post {
            try {
                val creditRequest = call.receive<CreditRequest>()
                val isValid = service.validateRequest(creditRequest)
                when (isValid.first) {
                    true -> {
                        val calculateCredit = service.calculateCredit(creditRequest)
                        when (calculateCredit.first) {
                            true -> call.respond(
                                HttpStatusCode.OK,
                                "%.2f".format(service.calculateCredit(creditRequest).second)
                            )
                            false -> call.respond(HttpStatusCode.OK, "Cannot credit")
                        }
                    }
                    false -> call.respond(HttpStatusCode.BadRequest, ValidationErrors(isValid.second))
                }
            } catch (e: Exception) {
                when (e) {
                    is kotlinx.serialization.SerializationException -> call.respond(
                        HttpStatusCode.BadRequest,
                        e.localizedMessage
                    )
                    else -> call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
                }
            }
        }
        get {
            call.respondText("Hello World!")
        }
    }
}
