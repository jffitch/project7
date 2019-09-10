package com.mathgeniusguide.project7.api

import com.mathgeniusguide.project7.responses.category.CategoryResponse
import com.mathgeniusguide.project7.responses.popular.PopularResponse
import com.mathgeniusguide.project7.responses.search.SearchResponseFull
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    // https://api.nytimes.com/svc/...?api-key=W0yBJTOlR8IoXFBrcyH5w09uWw4DvTvO
    @GET("topstories/v2/home.json")
    suspend fun getTopStories(): Response<CategoryResponse>

    @GET("mostpopular/v2/mostviewed/all-sections/7.json")
    suspend fun getPopularNews(): Response<PopularResponse>

    @GET("topstories/v2/politics.json")
    suspend fun getPoliticsNews(): Response<CategoryResponse>

    @GET("search/v2/articlesearch.json")
    suspend fun getSearchNews(
        @Query("q") query: String,
        @Query("fq") categories: String,
        @Query("begin_date") beginDate: String,
        @Query("end_date") endDate: String) : Response<SearchResponseFull>

    @GET("search/v2/articlesearch.json")
    fun getSearchNewsNotSuspended(
        @Query("q") query: String,
        @Query("fq") categories: String,
        @Query("begin_date") beginDate: String,
        @Query("end_date") endDate: String) : Call<SearchResponseFull>
}