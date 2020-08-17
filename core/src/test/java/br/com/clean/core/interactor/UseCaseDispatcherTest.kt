package br.com.clean.core.interactor

import br.com.clean.core.base.BaseUseCaseTest
import br.com.clean.core.business.interactor.CallbackDecorator
import br.com.clean.core.business.interactor.UseCaseDispatcher
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService

internal class UseCaseDispatcherTest : BaseUseCaseTest() {
    private lateinit var executeOn: ExecutorService
    private lateinit var resultOn: ExecutorService
    private lateinit var dispatcher: UseCaseDispatcher<String, String>

    @BeforeEach
    fun setupTest() {
        setupExecutorExecuteOn()
        setupExecutorResultOn()
        setupDispatcher()
    }

    abstract inner class BothThreadsExecutionTest {
        @Test
        fun `when dispatcher is invoked, then code runs on both threads`() {
            invokeDispatcher()
            assertExecuteOnInvocations(1)
            assertResultOnInvocations(1)
        }
    }

    abstract inner class GivenGuardAllowance : BothThreadsExecutionTest() {
        @BeforeEach
        fun setupTest() {
            arrangeGuardReturn(true)
        }
    }

    abstract inner class GivenCallback : GivenGuardAllowance() {
        lateinit var callback: UseCaseCallback<String>
        lateinit var decorator: CallbackDecorator<String, String>
        lateinit var dispatcher: UseCaseDispatcher<String, String>

        @BeforeEach
        fun setupCallbackTest() {
            callback = spy(UseCaseCallback())
            decorator = spy(CallbackDecorator(useCase, callback::run))
            dispatcher = spy(UseCaseDispatcher(decorator, executeOn.asCoroutineDispatcher(), resultOn.asCoroutineDispatcher()))
        }
    }

    @Nested
    @DisplayName("Given guard denial")
    inner class GuardDenialTest {

        @BeforeEach
        fun setupTest() {
            arrangeGuardReturn(false)
        }

        @Test
        fun `when dispatcher is invoked, then code only runs on executeOn thread`() {
            invokeDispatcher()
            assertExecuteOnInvocations(1)
            assertResultOnInvocations(0)
        }

        @Test
        fun `when dispatcher is invoked, then call onGuardError`() {
            invokeDispatcher()
            assertMethodCalls("!PROC", "GRD", "!EXE", "!RES", "!ERR")
        }

    }

    @Nested
    @DisplayName("Given guard raises exception")
    inner class GuardRaisesExceptionTest : BothThreadsExecutionTest() {

        private lateinit var exception: Exception

        @BeforeEach
        fun setupTest() {
            exception = arrangeException(useCase.guard(any()))
        }

        @Test
        fun `when dispatcher is invoked, then call onError`() {
            invokeDispatcher()
            assertMethodCalls("!PROC", "GRD", "!EXE")
            assertOnErrorException(exception = exception)
            assertResultError()
        }

    }

    @Nested
    @DisplayName("Given guard allowance")
    inner class GuardAllowanceWithoutCallbackTest : GivenGuardAllowance() {
        @Test
        fun `when dispatcher is invoked, then call execute and onResult with success`() {
            invokeDispatcher()
            assertMethodCalls("!PROC", "GRD", "EXE", "RES", "!ERR")
            assertResultSuccess()
        }
    }

    @Nested
    @DisplayName("Given guard allowance and callback")
    inner class GuardAllowanceWithCallbackTest : GivenCallback() {
        @Test
        fun `when dispatcher is invoked, then call execute and onResult with success`() {
            decorator.process(param)

            assertMethodCalls("!PROC", "GRD", "EXE", "RES", "!ERR")
            verify(decorator, times(1)).onResult(eq(output))
            verify(callback, times(1)).run(eq(output))
        }
    }

    @Nested
    @DisplayName("Given execution raises exception")
    inner class ExecutionRaisesExceptionTest : BothThreadsExecutionTest() {
        private lateinit var exception: Exception

        @BeforeEach
        fun setupTest() {
            arrangeGuardReturn(true)
            exception = arrangeException(useCase.execute(any()))
        }

        @Test
        fun `when dispatcher is invoked, then call onError and onResult with error`() {
            invokeDispatcher()
            assertMethodCalls("!PROC", "GRD", "EXE")
            assertOnErrorException(exception = exception)
            assertResultError()
        }
    }

    private fun setupExecutorResultOn() {
        resultOn = mock()
        setupExecutorAnswer(resultOn)
    }

    private fun setupExecutorExecuteOn() {
        executeOn = mock()
        setupExecutorAnswer(executeOn)
    }

    private fun setupDispatcher() {
        val dispatcher = UseCaseDispatcher(
                useCase = useCase,
                executeOn = executeOn.asCoroutineDispatcher(),
                resultOn = resultOn.asCoroutineDispatcher()
        )

        this.dispatcher = spy(dispatcher)
    }

    private fun invokeDispatcher() {
        runBlocking {
            dispatcher.dispatch(param)
        }
    }

    private fun assertExecuteOnInvocations(numInvocations: Int) {
        verify(executeOn, times(numInvocations)).execute(any())
    }

    private fun assertResultOnInvocations(numInvocations: Int) {
        verify(resultOn, times(numInvocations)).execute(any())
    }
}