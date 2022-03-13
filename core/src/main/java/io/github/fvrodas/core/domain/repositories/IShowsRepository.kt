package io.github.fvrodas.core.domain.repositories

import io.github.fvrodas.core.domain.entities.ShowEntity

interface IShowsRepository {
    suspend fun getSeriesListByPageNumber(pageNumber: Long): Result<List<ShowEntity>>
    suspend fun searchSeriesByName(name: String): Result<List<ShowEntity>>
    suspend fun getSeriesDetailByID(ID: Long): Result<ShowEntity>
}