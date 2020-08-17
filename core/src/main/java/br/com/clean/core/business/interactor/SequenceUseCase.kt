package br.com.clean.core.business.interactor

import br.com.clean.core.business.dto.Output
import br.com.clean.core.business.dto.ValueOutput

open class SequenceUseCase(private val units: List<UseCaseUnit<*, *>>) :
        UseCase<Nothing, List<Output<*>>>() {
    private val stream: MutableList<Output<*>> = mutableListOf()

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    override fun execute(param: Nothing?): Output<List<Output<*>>> {
        for (unit in units) {
            val output = unit.process()
            stream.add(output)
        }

        return ValueOutput(stream)
    }

    class Builder {
        private val list = mutableListOf<UseCaseUnit<*, *>>()
        fun <P, R> add(useCase: UseCase<P, R>, param: P? = null): Builder {
            list.add(UseCaseUnit(useCase, param))
            return this
        }

        fun build(): UseCase<Nothing, List<Output<*>>> {
            return SequenceUseCase(list)
        }
    }
}