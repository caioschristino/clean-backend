package org.example.feature

import com.nhaarman.mockitokotlin2.*
import org.example.landing.business.LandingRepository
import org.example.landing.business.LandingUseCase
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.spy
import java.lang.RuntimeException

class LandingUseCaseTest {
    private val param = null
    private lateinit var useCase: LandingUseCase
    private lateinit var repo: LandingRepository

    @Before
    fun setup() {
        repo = mock()
        useCase = spy(LandingUseCase(repo))
    }

    @Test
    fun `when allowed by guard, then call onSuccess function`() {
        useCase.process(param)
        verify(useCase, times(1)).onResult(argThat { this.isSuccess() })
    }

    @Test
    fun `when denied by guard, then call onGuardError function`() {
        val exception = RuntimeException()
        whenever(useCase.guard(eq(param))).thenThrow(exception)

        useCase.process(param)

        verify(useCase, times(1)).onResult(argThat { this.isError() })
    }

    @Test
    fun `when guard throws error, then call onError function`() {
        val exception = RuntimeException()
        whenever(useCase.guard(eq(param))).thenThrow(exception)

        useCase.process(param)

        verify(useCase, times(1)).onError(eq(exception))
        verify(useCase, times(0)).onResult(argThat { this.isSuccess() })
    }

    @Test
    fun `when execute throws error, then call onError function`() {
        val exception = RuntimeException()
        whenever(useCase.execute(eq(param))).thenThrow(exception)

        useCase.process(param)

        verify(useCase, times(1)).onError(eq(exception))
        verify(useCase, times(0)).onResult(argThat { this.isSuccess() })
    }
}