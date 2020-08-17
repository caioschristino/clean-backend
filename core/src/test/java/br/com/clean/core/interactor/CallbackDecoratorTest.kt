package br.com.clean.core.interactor

import br.com.clean.core.base.BaseUseCaseTest
import br.com.clean.core.business.dto.Output
import br.com.clean.core.business.interactor.CallbackDecorator
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test

internal class CallbackDecoratorTest : BaseUseCaseTest() {

    @Test
    fun callback() {
        val callback = spy(Callback())
        val decorator = spy(CallbackDecorator(useCase, callback::set))
        whenever(decorator.guard(eq(param))).thenReturn(true)

        decorator.process(param)

        verify(decorator, times(1)).onResult(any())
    }

    open class Callback {
        lateinit var data: Output<String>
        fun set(data: Output<String>) {
            this.data = data
        }
    }
}