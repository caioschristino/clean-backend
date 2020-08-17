package br.com.clean.core.business.controller

interface ControllerFactory<V, T : Controller> {
    fun create(context: V): T
}