package br.com.clean.core.business.controller

import br.com.clean.core.business.dto.Output
import br.com.clean.core.business.interactor.*
import kotlinx.coroutines.Job

abstract class BaseController : Controller {
    private val compositeJobDisposable = CompositeJobDisposable()

    fun disposeAll() {
        compositeJobDisposable.cancel()
    }

    protected open fun <P, R> dispatchUseCase(
            param: P?,
            useCase: UseCase<P, R>,
            listener: (Output<R>) -> Unit
    ): Job? {
        val dispatcher = UseCaseDispatcher(CallbackDecorator(useCase, listener))
        val job = dispatcher.dispatch(param)
        compositeJobDisposable.add(job)
        return job
    }

    protected open fun <P, R> processUseCase(
            param: P?,
            useCase: UseCase<P, R>
    ): Output<R>? {
        val callback = UseCaseUnit.Callback<R>()
        CallbackDecorator(useCase, callback::set).process(param)

        return callback.output
    }
}