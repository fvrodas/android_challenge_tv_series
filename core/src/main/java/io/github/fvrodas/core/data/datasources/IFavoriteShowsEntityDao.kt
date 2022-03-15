package io.github.fvrodas.core.data.datasources

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.fvrodas.core.domain.entities.ShowEntity

@Dao
interface IFavoriteShowsEntityDao {

    @Query("SELECT * FROM ShowEntity")
    fun getAll(): List<ShowEntity>

    @Insert
    fun insertAll(vararg stories: ShowEntity)

    @Delete
    fun delete(story: ShowEntity)
}