package io.github.fvrodas.core.data.models

import io.github.fvrodas.core.domain.entities.EpisodeEntity
import io.github.fvrodas.core.domain.entities.ShowEntity

data class Show(
    val id: Long,
    val url: String,
    val name: String?,
    val type: String,
    val language: String,
    val genres: List<String>,
    val status: String,
    val runtime: Long?,
    val averageRuntime: Long?,
    val premiered: String,
    val ended: String,
    val officialSite: String,
    val schedule: Schedule,
    val rating: Rating,
    val weight: Long?,
    val network: Network,
    val webChannel: Network,
    val dvdCountry: Any? = null,
    val externals: Externals,
    val image: Image?,
    val summary: String,
    val updated: Long?
) : BaseResponse() {
    fun asEntity(): ShowEntity = ShowEntity(
        this.id,
        this.name,
        this.url,
        this.image?.medium,
        this.image?.original,
        this.summary,
        this.genres,
        this.status,
        this.schedule,
        this._embedded?.episodes?.map { it.asEntity() }
    )

}