package org.example.plugin.api

import com.google.gson.Gson
import okhttp3.Interceptor
import org.example.plugin.api.AbstractBuilder
import org.example.plugin.api.PokedexAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokedexAPIBuilder(baseUrl: String?, interceptors: List<Interceptor> = emptyList()) :
    AbstractBuilder<PokedexAPI>(baseUrl, interceptors) {

    override fun build(): PokedexAPI {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createHttpClient())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(PokedexAPI::class.java)
    }
}