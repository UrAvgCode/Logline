package com.moritz.movieappuitest.api.tmdb

import com.moritz.movieappuitest.dataclasses.MovieCollection
import com.moritz.movieappuitest.dataclasses.MovieCredits
import com.moritz.movieappuitest.dataclasses.MovieDetails
import com.moritz.movieappuitest.dataclasses.MovieList
import com.moritz.movieappuitest.utils.TmdbCredentials
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {
    @GET("discover/movie")
    suspend fun getPopularMovies(
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("api_key") apiKey: String = TmdbCredentials.API_KEY
    ): Response<MovieList>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = TmdbCredentials.API_KEY
    ): Response<MovieList>

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = TmdbCredentials.API_KEY
    ): Response<MovieList>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") searchQuery: String?,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = TmdbCredentials.API_KEY
    ): Response<MovieList>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int?,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = TmdbCredentials.API_KEY
    ): Response<MovieCredits>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int?,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = TmdbCredentials.API_KEY
    ): Response<MovieDetails>

    @GET("collection/{collection_id}")
    suspend fun getMoviesFromCollection(
        @Path("collection_id") collectionId: Int?,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = TmdbCredentials.API_KEY
    ): Response<MovieCollection>
}