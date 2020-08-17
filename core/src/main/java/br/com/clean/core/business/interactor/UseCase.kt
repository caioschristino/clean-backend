package br.com.clean.core.business.interactor


import br.com.clean.core.business.dto.ErrorOutput
import br.com.clean.core.business.dto.Output
import com.google.common.annotations.VisibleForTesting

abstract class UseCase<P, R> {
    open fun process(param: P? = null) {
        try {
            if (guard(param)) execute(param).also {
                onResult(it)
            }
        } catch (error: Throwable) {
            onError(error)
        }
    }

    abstract fun execute(param: P? = null): Output<R>

    @VisibleForTesting
    open fun onError(error: Throwable) = onResult(ErrorOutput(error))

    @VisibleForTesting
    open fun onResult(output: Output<R>) {
    }

    @VisibleForTesting
    open fun guard(param: P? = null): Boolean = true
}