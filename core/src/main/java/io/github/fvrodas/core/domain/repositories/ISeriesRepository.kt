package io.github.fvrodas.core.domain.repositories

import io.github.fvrodas.core.domain.entities.SeriesEntity

interface ISeriesRepository {
    suspend fun getSeriesListByPageNumber(pageNumber: Long): Result<List<SeriesEntity>>
    suspend fun searchSeriesByName(name: String): Result<List<SeriesEntity>>
    suspend fun getSeriesDetailByID(ID: Long): Result<SeriesEntity>
}