package br.com.clean.core.base

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.whenever
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.ExecutorService

abstract class AbstractTest {
    protected fun <T> arrangeException(method: T): Exception {
        val exception = RuntimeException()
        whenever(method).thenThrow(exception)
        return exception
    }

    protected fun setupExecutorAnswer(service: ExecutorService) {
        doAnswer {
            val runnable = it.arguments[0] as Runnable
            runnable.run()
            null
        }.`when`(service).execute(any())
    }
}