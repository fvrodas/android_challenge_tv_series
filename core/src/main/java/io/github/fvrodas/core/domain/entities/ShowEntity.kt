package io.github.fvrodas.core.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import io.github.fvrodas.core.data.models.Schedule
import java.io.Serializable

@Entity
data class ShowEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "poster") val poster: String?,
    @ColumnInfo(name = "posterHQ") val posterHQ: String?,
    @ColumnInfo(name = "summary") val summary: String?,
    @Ignore val genres: List<String>? = null,
    @ColumnInfo(name = "status") val status: String?,
    @Ignore val schedule: Schedule? = null,
    @Ignore val episodes: List<EpisodeEntity>? = null
) : Serializable {

    constructor(
        id: Long,
        name: String?,
        url: String?,
        poster: String?,
        posterHQ: String?,
        summary: String?,
        status: String?
    ) : this(id, name, url, poster, posterHQ, summary, null, status, null, null)

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}