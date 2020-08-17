package org.example.landing.business

import org.example.landing.domain.Landing

interface LandingRepository {
    fun doFetch(): Landing?
}