package io.github.fvrodas.core.data.models

import java.io.Serializable

data class Schedule (
    val time: String,
    val days: List<String>
) : Serializable