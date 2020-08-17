package br.com.clean.core.interactor

import br.com.clean.core.base.BaseUseCaseTest
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test

internal class UseCaseTest : BaseUseCaseTest() {

    @Test
    fun `given guard denial, when use case start then call no other`() {
        arrangeGuardReturn(false)

        useCase.process(param)

        assertMethodCalls("PROC", "GRD", "!EXE", "!RES", "!ERR")
    }

    @Test
    fun `given guard allowance, then call execute, onResult only`() {
        arrangeGuardReturn(true)

        useCase.process(param)

        assertMethodCalls("PROC", "GRD", "EXE", "RES", "!ERR")
    }

    @Test
    fun `given guard raises exception, then call onError only`() {
        val exception = RuntimeException()
        whenever(useCase.guard(eq(param))).thenThrow(exception)

        useCase.process(param)

        assertMethodCalls("PROC", "GRD", "!EXE", "ERR")
        assertOnErrorException(exception = exception)
        assertResultError()
    }

    @Test
    fun `given execute raises exception, then call execute, onError only`() {
        val exception = RuntimeException()
        arrangeGuardReturn(true)
        whenever(useCase.execute(eq(param))).thenThrow(exception)

        useCase.process(param)

        assertMethodCalls("PROC", "GRD", "EXE", "ERR")
        assertOnErrorException(exception = exception)
        assertResultError()
    }
}