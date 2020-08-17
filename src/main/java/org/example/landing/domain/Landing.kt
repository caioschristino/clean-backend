package org.example.landing.domain

open class Landing(
        val count: Int,
        val next: String,
        var results: MutableList<Pokemon>)