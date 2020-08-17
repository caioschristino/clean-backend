package org.example

import org.example.plugin.feature.landing.LandingRepositoryImpl
import org.example.landing.business.LandingRepository
import org.example.landing.business.LandingUseCase
import org.example.landing.gateway.LandingGatewayInjector
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application {
    init {
        LandingGatewayInjector.self = object : LandingGatewayInjector {
            override val doFetch: LandingUseCase
                get() = LandingUseCase(injectaCacheRepository())
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}


private fun injectaCacheRepository(): LandingRepository {
    val baseApiUrl = "https://pokeapi.co/api/v2/"
    return LandingRepositoryImpl(baseApiUrl)
}