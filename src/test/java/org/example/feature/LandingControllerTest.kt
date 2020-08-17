package org.example.feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import org.example.base.BaseControllerTest
import org.example.landing.business.LandingRepository
import org.example.landing.business.LandingUseCase
import org.example.landing.domain.Landing
import org.example.landing.gateway.LandingGatewayInjector
import org.example.plugin.feature.landing.gateway.LandingImplController
import org.junit.After
import org.junit.Before
import org.junit.Test

class LandingControllerTest : BaseControllerTest<LandingImplController>() {
    private lateinit var repository: LandingRepository

    @Before
    override fun setup() {
        super.setup()
    }

    @After
    override fun teardown() {
        super.teardown()
    }

    override fun setupController() {
        repository = mock()
        LandingGatewayInjector.self = object : LandingGatewayInjector {
            override val doFetch: LandingUseCase
                get() = LandingUseCase(repository)
        }
        controller = spy(LandingImplController())
    }

    @Test
    fun `when fetch succeeds, assert callbacks`() {
        val result = arrangeFetch()
        doGet()
        assertViewStateSuccess(result)
    }

    @Test
    fun `when fetch throws exception, assert callbacks`() {
        val exception = RuntimeException()
        whenever(repository.doFetch()).thenThrow(exception)
        doGet()
        assertViewStateError(exception)
    }

    private fun doGet() {
//        runBlocking { viewModel?.doFetch(DataFactory.randomUuid()) }
    }


    private fun arrangeFetch(): Landing {
        val result = mock<Landing>()
        whenever(repository.doFetch()).thenReturn(result)

        return result
    }
}