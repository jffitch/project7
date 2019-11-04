package com.mathgeniusguide.project7.api

import com.mathgeniusguide.project7.connectivity.ConnectivityInterceptor
import com.mathgeniusguide.project7.responses.category.CategoryResponse
import com.mathgeniusguide.project7.responses.popular.PopularResponse
import com.mathgeniusguide.project7.responses.search.SearchResponseFull
import com.mathgeniusguide.project7.util.Constants
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

    // for use in JobService
    @GET("search/v2/articlesearch.json")
    fun getSearchNewsNotSuspended(
        @Query("q") query: String,
        @Query("fq") categories: String,
        @Query("begin_date") beginDate: String,
        @Query("end_date") endDate: String) : Call<SearchResponseFull>


    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): Api {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api-key", Constants.API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            val userMoshi = Moshi
                .Builder()
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(userMoshi))
                .build()
                .create(Api::class.java)
        }
    }
}