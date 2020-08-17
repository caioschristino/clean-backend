package br.com.clean.core.business.interactor

import br.com.clean.core.business.dto.ErrorOutput
import br.com.clean.core.business.dto.Output

open class CallbackDecorator<P,R>(useCase: UseCase<P,R>, private val callback: (Output<R>)->Unit):
        UseCaseDecorator<P,R>(useCase) {
    override fun onResult(output: Output<R>) {
        super.onResult(output)
        callback.invoke(output)
    }

    override fun onError(error: Throwable) {
        super.onError(error)
        callback.invoke(ErrorOutput(error))
    }
}
