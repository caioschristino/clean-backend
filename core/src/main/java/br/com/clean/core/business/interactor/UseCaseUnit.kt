package br.com.clean.core.business.interactor

import br.com.clean.core.business.dto.Output
import br.com.clean.core.business.dto.ValueOutput

data class UseCaseUnit<P, R>(val useCase: UseCase<P, R>, val param: P?) {
    fun process(): Output<R> {
        val callback = Callback<R>()
        val decorator = CallbackDecorator(useCase, callback::set)
        decorator.process(param)
        return callback.output
    }

    open class Callback<R> {
        var output: Output<R> = ValueOutput()
        fun set(value: Output<R>) {
            output = value
        }
    }
}