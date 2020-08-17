package org.example.landing.gateway

import org.example.landing.business.LandingUseCase

interface LandingGatewayInjector {
    companion object {
        lateinit var self: LandingGatewayInjector
    }

    val doFetch: LandingUseCase
}