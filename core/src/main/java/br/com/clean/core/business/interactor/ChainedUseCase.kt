package br.com.clean.core.business.interactor

import br.com.clean.core.business.dto.ErrorOutput
import br.com.clean.core.business.dto.Output

class ChainedUseCase<P, R, T>(private val first: UseCase<P, R>, private val second: UseCase<R, T>) :
        UseCase<P, T>() {
    override fun execute(param: P?): Output<T> {
        val intermediate = first.execute(param)
        if (intermediate.isSuccess()) {
            return second.execute(intermediate.value)
        }
        return ErrorOutput(intermediate.error)
    }
}