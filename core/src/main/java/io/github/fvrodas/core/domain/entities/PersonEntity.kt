package io.github.fvrodas.core.domain.entities

import io.github.fvrodas.core.data.models.Image
import java.io.Serializable

data class PersonEntity(
    val id: Long,
    val name: String,
    val gender: String,
    val image: Image?
) : Serializable