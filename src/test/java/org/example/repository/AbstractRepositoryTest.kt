package org.example.repository

import okhttp3.mockwebserver.MockWebServer
import org.example.plugin.api.BaseRepository

abstract class AbstractRepositoryTest<T : BaseRepository> {
    protected val server = MockWebServer()
    protected lateinit var repository: T

    open fun setup() {
        server.start()
        setupRepository()
    }

    abstract fun setupRepository()

    protected open fun setupUrl(): String {
        val baseUrl = "/"
        return server.url(baseUrl).toString()
    }
}