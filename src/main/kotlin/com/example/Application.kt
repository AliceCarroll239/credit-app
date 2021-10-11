package com.example

import com.example.repository.CreditRepository
import com.example.service.CreditService
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.web.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.single

val creditAppModule = module {
    single<CreditService>() bind CreditService::class
    single<CreditRepository>()
}

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }
    install(CORS) {
        anyHost()
    }
    install(org.koin.ktor.ext.Koin) {
        modules(creditAppModule)
    }

    install(Routing) {
        credit()
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}
