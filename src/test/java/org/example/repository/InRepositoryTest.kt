package org.example.repository

import okhttp3.mockwebserver.MockResponse
import org.example.plugin.api.BaseRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

abstract class InRepositoryTest<T : BaseRepository> : AbstractRepositoryTest<T>() {
    protected val authToken = "token"

    @Before
    override fun setup() {
        super.setup()
    }

    final override fun setupRepository() {
        val httpUrl = setupUrl()
        setupRepository(httpUrl)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `when auth is required, then authorization header is set`() {
        enqueueEmptyResponse(200)

        repository.dumbRequest()

        val request = server.takeRequest()
        val header = request.getHeader("Authorization")
        Assert.assertEquals(header, null)
    }

    abstract fun setupRepository(httpUrl: String)

    protected fun enqueueEmptyResponse(code: Int) {
        val response = MockResponse().apply {
            setResponseCode(code)
            setBody("{}")
        }

        server.enqueue(response)
    }

    protected fun enqueueResponse(code: Int, body: String) {
        val response = MockResponse().apply {
            setResponseCode(code)
            setBody(body)
        }

        server.enqueue(response)
    }
}