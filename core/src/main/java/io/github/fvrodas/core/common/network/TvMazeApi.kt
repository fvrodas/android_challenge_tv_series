package io.github.fvrodas.core.common.network

import io.github.fvrodas.core.BuildConfig
import io.github.fvrodas.core.data.models.Person
import io.github.fvrodas.core.data.models.Show
import io.github.fvrodas.core.data.models.ShowSearchResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeApi {
    @GET("/shows")
    suspend fun getListOfShowsByPageNumber(
        @Query("page") pageNumber:Long
    ) : List<Show>

    @GET("/search/shows")
    suspend fun searchShowsByName(
        @Query("q") name: String?
    ): List<ShowSearchResult>

    @GET("/shows/{id}")
    suspend fun getShowDetailsById(
        @Path("id") ID:Long,
        @Query("embed") embed: String = "episodes"
    ): Show

    @GET("/people")
    suspend fun getListOfPeopleByPageNumber(
        @Query("page") pageNumber:Long
    ) : List<Person>

    @GET("/search/people")
    suspend fun searchPeopleByName(
        @Query("q") name: String?
    ): List<Person>

    @GET("/people/{id}/castcredits")
    suspend fun getPersonDetailsById(
        @Path("id") ID:Long,
        @Query("embed") embed: String = "show"
    ): Show

    companion object {
        val services: TvMazeApi by lazy {
            Retrofit.Builder().apply {
                baseUrl(BuildConfig.BASE_URL)
                addConverterFactory(MoshiConverterFactory.create())
                client(OkHttpClient.Builder().apply {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }.build())
            }.build().create(TvMazeApi::class.java)
        }
    }
}