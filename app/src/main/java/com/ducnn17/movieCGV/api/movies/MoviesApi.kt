package com.ducnn17.movieCGV.api.movies

import com.ducnn17.movieCGV.data.detailsmovies.DetailsMovies
import com.ducnn17.movieCGV.data.movies.entity.Movies
import com.ducnn17.movieCGV.data.movies.entity.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    fun getListMovies(@Query("page") page: Int): Call<Movies>

    @GET("movie/{id}?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    fun getMoviesDetails(@Path("id") id: Int): Call<Result>

    @GET("movie/{id}/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    fun getCastCrewList(@Path("id") id: Int): Call<DetailsMovies>

    @GET("3/movie/top_rated?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    fun getMovieListTopRate(@Query("api_key") apiKey: String  , @Query("page") pageNumber: String)

    @GET("3/movie/upcoming?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    fun getMovieListUpcoming(@Query("api_key") apiKey: String  , @Query("page") pageNumber: String)

}