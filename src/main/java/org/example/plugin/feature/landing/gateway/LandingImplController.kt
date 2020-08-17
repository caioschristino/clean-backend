package org.example.plugin.feature.landing.gateway

import br.com.clean.core.business.controller.BaseController
import org.example.landing.domain.Greeting
import org.example.landing.business.LandingUseCase
import org.example.landing.domain.Pokemon
import org.example.landing.gateway.LandingPresenter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.example.landing.gateway.LandingGatewayInjector.Companion.self as injector

@RestController
open class LandingImplController : BaseController(), LandingPresenter {
    private val fetcher by lazy { injectLanding() }

    protected fun injectLanding(): LandingUseCase {
        return injector.doFetch
    }

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
        val result = doFetch()

        return Greeting("Hello, ${result?.get(2)?.name}")
    }

    override fun doFetch(): List<Pokemon>? {
        return processUseCase(null, fetcher)?.value?.results
    }
}