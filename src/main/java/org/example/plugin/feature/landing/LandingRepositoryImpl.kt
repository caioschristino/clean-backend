package org.example.plugin.feature.landing

import org.example.plugin.api.BaseRepository
import org.example.plugin.api.PokedexAPI
import org.example.plugin.api.PokedexAPIBuilder
import okhttp3.Interceptor
import org.example.landing.business.LandingRepository
import org.example.landing.domain.Landing

open class LandingRepositoryImpl(url: String) :
        BaseRepository(url), LandingRepository {

    override fun doFetch(): Landing? {
        return getBodyOrThrow(getService().fetch())
    }

    override fun getService(interceptors: List<Interceptor>): PokedexAPI {
        return PokedexAPIBuilder(baseUrl).build()
    }
}