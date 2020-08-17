package br.com.clean.core.business.interactor

import br.com.clean.core.business.dto.Output

abstract class UseCaseDecorator<P, R>(private val useCase: UseCase<P, R>) : UseCase<P, R>() {

    override fun onError(error: Throwable) {
        useCase.onError(error)
    }

    override fun execute(param: P?): Output<R> {
        return useCase.execute(param)
    }

    override fun onResult(output: Output<R>) {
        useCase.onResult(output)
    }

    override fun guard(param: P?): Boolean {
        return useCase.guard(param)
    }
}