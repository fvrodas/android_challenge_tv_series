package io.github.fvrodas.core.domain.entities

import java.io.Serializable

data class PersonEntity(
    val id: Long,
    val name: String,
    val gender: String?,
    val image: String?,
    val imageHQ: String?,
    val shows: List<ShowEntity>?
) : Serializable