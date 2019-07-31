package com.mathgeniusguide.project7.api

import com.mathgeniusguide.project7.responses.category.CategoryResponse
import com.mathgeniusguide.project7.responses.popular.PopularResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    // https://api.nytimes.com/svc/...?api-key=W0yBJTOlR8IoXFBrcyH5w09uWw4DvTvO
    @GET("topstories/v2/home.json")
    suspend fun getTopStories(): Response<CategoryResponse>

    @GET("mostpopular/v2/mostviewed/all-sections/7.json")
    suspend fun getPopularNews(): Response<PopularResponse>

    @GET("topstories/v2/business.json")
    suspend fun getBusinessNews(): Response<CategoryResponse>

    @GET("topstories/v2/politics.json")
    suspend fun getPoliticsNews(): Response<CategoryResponse>
}