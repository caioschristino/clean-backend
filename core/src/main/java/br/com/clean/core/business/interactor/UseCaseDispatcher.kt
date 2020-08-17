package br.com.clean.core.business.interactor

import br.com.clean.core.business.dto.Output
import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.*

open class UseCaseDispatcher<P, R>(useCase: UseCase<P, R>,
                                   executeOn: CoroutineDispatcher = Dispatchers.IO,
                                   resultOn: CoroutineDispatcher = Dispatchers.Main) {
    private val decorator: DispatcherDecorator<P, R>

    init {
        decorator = DispatcherDecorator(useCase, executeOn, resultOn)
    }

    fun dispatch(param: P? = null): Job? {
        return decorator.dispatch(param)
    }

    private class DispatcherDecorator<P, R>(
            private val useCase: UseCase<P, R>,
            private val executeOn: CoroutineDispatcher = Dispatchers.IO,
            private val resultOn: CoroutineDispatcher = Dispatchers.Main) : UseCaseDecorator<P, R>(useCase) {

        fun dispatch(param: P? = null): Job? {
            return GlobalScope.launch(executeOn) { process(param) }
        }

        override fun onError(error: Throwable) {
            GlobalScope.launch(resultOn) { useCase.onError(error) }
        }

        override fun onResult(output: Output<R>) {
            GlobalScope.launch(resultOn) { useCase.onResult(output) }
        }
    }
}