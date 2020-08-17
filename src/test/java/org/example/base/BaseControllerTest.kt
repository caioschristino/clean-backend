package org.example.base

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Rule
import org.junit.rules.TestRule

abstract class BaseControllerTest<T> {
    protected var controller: T? = null
//    @get:Rule
//    var rule: TestRule = InstantTaskExecutorRule()

    open fun setup() {
        setupController()
    }

    protected abstract fun setupController()

    protected fun assertViewStateError(exception: Exception) {

    }

    protected fun assertViewStateSuccess(value: Any) {

    }

    open fun teardown() {
//        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}
