package br.com.clean.core.business.dto

abstract class Output<V>(val value: V?=null, val error: Throwable? = null) {
    open fun isError(): Boolean = error != null
    fun isSuccess(): Boolean = !isError()
    fun isEmpty(): Boolean = value != null
}