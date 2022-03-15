package io.github.fvrodas.core.domain.entities

import java.io.Serializable

data class EpisodeEntity (
    val id: Long,
    val name: String,
    val number: Long,
    val season: Long,
    val summary: String?,
    val image: String?,
    val imageHQ: String?
) : Serializable