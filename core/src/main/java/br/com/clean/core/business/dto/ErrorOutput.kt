package br.com.clean.core.business.dto

class ErrorOutput<V>(error: Throwable?, value: V?=null): Output<V>(value, error) {
    override fun isError(): Boolean = true
}