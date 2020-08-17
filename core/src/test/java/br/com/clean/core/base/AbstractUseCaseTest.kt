package br.com.clean.core.base

import br.com.clean.core.business.dto.Output
import br.com.clean.core.business.dto.ValueOutput
import br.com.clean.core.business.interactor.UseCase
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach

abstract class AbstractUseCaseTest<P : Any, R : Any> : AbstractTest() {
    protected lateinit var useCase: UseCase<P, R>
    protected lateinit var result: R
    protected lateinit var output: Output<R>
    protected lateinit var param: P

    @BeforeEach
    fun setup() {
        prepareParam()
        prepareResult()
        prepareOutput()
        prepareUseCase()
    }

    open fun prepareResult() {}
    open fun prepareParam() {}

    protected open fun prepareOutput() {
        output = ValueOutput(result)
    }

    protected open fun prepareUseCase() {
        useCase = createUseCase(param, output)
    }

    protected open fun createUseCase(param: P, result: Output<R>): UseCase<P, R> {
        val useCase: UseCase<P, R> = mock()
        whenever(useCase.process(eq(param))).thenCallRealMethod()
        whenever(useCase.execute(eq(param))).thenReturn(result)
        whenever(useCase.onError(any())).thenCallRealMethod()
        whenever(useCase.onResult(any())).thenCallRealMethod()
        return useCase
    }

    protected fun arrangeGuardReturn(value: Boolean) {
        whenever(useCase.guard(eq(param))).thenReturn(value)
    }

    protected fun assertOnErrorException(exception: Exception) {
        verify(useCase, times(1)).onError(eq(exception))
    }

    protected fun assertResultSuccess() {
        verify(useCase, times(1)).onResult(argThat { this.isSuccess() })
    }

    protected fun assertResultError() {
        verify(useCase, times(1)).onResult(argThat { this.isError() })
    }

    protected fun assertMethodCalls(vararg args: String) {
        val executeMap = mapOf(
                "EXE" to { verify(useCase, times(1)).execute(eq(param)) },
                "!EXE" to { verify(useCase, times(0)).execute(eq(param)) }
        )

        val callbackMap = mapOf(
                "PROC" to { verify(useCase, times(1)).process(eq(param)) },
                "!PROC" to { verify(useCase, times(0)).process(eq(param)) },
                "GRD" to { verify(useCase, times(1)).guard(eq(param)) },
                "!GRD" to { verify(useCase, times(0)).guard(eq(param)) },
                "RES" to { verify(useCase, times(1)).onResult(eq(output)) },
                "!RES" to { verify(useCase, times(0)).onResult(any()) },
                "ERR" to { verify(useCase, times(1)).onError(any()) },
                "!ERR" to { verify(useCase, times(0)).onError(any()) }
        )

        for (arg in args) {
            executeMap[arg]?.invoke()
            callbackMap[arg]?.invoke()
        }
    }

    open class UseCaseCallback<R> {
        lateinit var result: Output<R>

        fun run(value: Output<R>) {
            this.result = value
        }
    }
}


abstract class BaseUseCaseTest : AbstractUseCaseTest<String, String>() {
    override fun prepareParam() {
        param = "param"
    }

    override fun prepareResult() {
        result = "result"
    }
}