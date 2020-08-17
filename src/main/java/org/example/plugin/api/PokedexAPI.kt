package org.example.plugin.api

import org.example.landing.domain.Landing
import retrofit2.Call
import retrofit2.http.GET

interface PokedexAPI {
    @GET("pokemon")
    fun fetch(): Call<Landing>

    @GET("test")
    fun dumbRequest(): Call<Any?>
}