package io.github.fvrodas.core.data.models

import io.github.fvrodas.core.domain.entities.EpisodeEntity

data class Episode (
    val id: Long,
    val url: String,
    val name: String,
    val season: Long,
    val number: Long,
    val type: String,
    val airdate: String,
    val airtime: String,
    val airstamp: String,
    val runtime: Long,
    val rating: Rating,
    val image: Image?,
    val summary: String
) : BaseResponse() {
    fun asEntity() : EpisodeEntity {
        return EpisodeEntity(
            this.id,
            this.name,
            this.number,
            this.season,
            this.summary,
            this.image?.medium,
            this.image?.original
        )
    }
}