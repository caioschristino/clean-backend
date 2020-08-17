package br.com.clean.core.business.dto

open class ValueOutput<V>(value: V? = null) : Output<V>(value, null) {
    override fun isError(): Boolean = false
}