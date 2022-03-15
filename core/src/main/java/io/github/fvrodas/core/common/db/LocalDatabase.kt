package io.github.fvrodas.core.common.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.fvrodas.core.data.datasources.IFavoriteShowsEntityDao
import io.github.fvrodas.core.domain.entities.ShowEntity

@Database(entities = [ShowEntity::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun favoriteShowsDao(): IFavoriteShowsEntityDao
}

class DatabaseFactory(app: Application) {
    val db: LocalDatabase by lazy {
        Room.databaseBuilder(app, LocalDatabase::class.java, "favorite_shows_db").build()
    }
}