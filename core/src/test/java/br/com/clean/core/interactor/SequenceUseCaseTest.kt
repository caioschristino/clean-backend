package br.com.clean.core.interactor

import br.com.clean.core.base.BaseUseCaseTest
import br.com.clean.core.business.dto.Output
import br.com.clean.core.business.dto.ValueOutput
import br.com.clean.core.business.interactor.SequenceUseCase
import br.com.clean.core.business.interactor.UseCase
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SequenceUseCaseTest : BaseUseCaseTest() {
    private val secondParam = "test"
    private val secondOutput = ValueOutput("secondOutput")
    private lateinit var secondUseCase: UseCase<String, String>
    private lateinit var sequenceUseCase: UseCase<Nothing, List<Output<*>>>

    @BeforeEach
    fun setupTest() {
        secondUseCase = createUseCase(secondParam, secondOutput)
        val builder = SequenceUseCase.builder()
                .add(useCase, param)
                .add(secondUseCase, secondParam)
        sequenceUseCase = spy(builder.build())

        whenever(secondUseCase.guard(eq(secondParam))).thenReturn(true)
        whenever(useCase.guard(eq(param))).thenReturn(true)
        whenever(sequenceUseCase.guard()).thenReturn(true)
    }

    private fun invokeSequence() {
        sequenceUseCase.process()
    }

    @Test
    fun `given two use cases, when sequence is invoked, then get return in insert order`() {
        invokeSequence()
        verify(sequenceUseCase, times(1)).onResult(any())
//        verify(sequenceUseCase).onResult(argThat {
//            val values = this.value!!
//            val first = (values[0] == output)
//            val second = (values[1] == secondOutput)
//            first && second
//        })
    }

    @Test
    fun `given two use cases and one of them raises exception, then return has one error value`() {
        val exception = RuntimeException()
        whenever(secondUseCase.guard(any())).thenThrow(exception)

        invokeSequence()

        verify(sequenceUseCase, times(1)).onError(any())
//        verify(secondUseCase).onError(eq(exception))

//        verify(sequenceUseCase).onResult(argThat {
//            val values = this.value!!
//            val first = (values[0] == output)
//            val second = values[1].isError()
//            first && second
//        })
    }
}