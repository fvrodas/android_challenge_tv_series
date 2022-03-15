package io.github.fvrodas.core.domain.entities

import io.github.fvrodas.core.data.models.Schedule
import java.io.Serializable

data class ShowEntity(
    val id: Long?,
    val name: String?,
    val url: String?,
    val poster: String?,
    val posterHQ: String?,
    val summary: String?,
    val genres: List<String>?,
    val status: String?,
    val schedule: Schedule?,
    val episodes: List<EpisodeEntity>?
) : Serializable {
    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}