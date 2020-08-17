package org.example.landing.gateway

import br.com.clean.core.business.controller.Controller
import org.example.landing.domain.Pokemon


interface LandingPresenter : Controller {
    fun doFetch(): List<Pokemon>?
}