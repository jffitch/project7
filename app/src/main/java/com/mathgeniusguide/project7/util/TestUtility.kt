package com.mathgeniusguide.project7.util

import com.mathgeniusguide.project7.responses.category.CategoryResult
import com.mathgeniusguide.project7.responses.popular.PopularResult
import com.mathgeniusguide.project7.responses.search.SearchHeadline
import com.mathgeniusguide.project7.responses.search.SearchResult
import java.util.*
import kotlin.collections.ArrayList

object TestUtility {
    fun getTestingCategoryListOfSize(size: Int): List<CategoryResult> {
        val list = ArrayList<CategoryResult>()
        for (i in 0 until size) {
            val result = CategoryResult(
                created_date = generateRandomString(),
                multimedia = emptyList(),
                section = generateRandomString(),
                subsection = generateRandomString(),
                title = generateRandomString(),
                url = generateRandomString()
            )
            list.add(result)
        }
        return list
    }

    fun getTestingPopularListOfSize(size: Int): List<PopularResult> {
        val list = ArrayList<PopularResult>()
        for (i in 0 until size) {
            val result = PopularResult(
                published_date = generateRandomString(),
                media = emptyList(),
                section = generateRandomString(),
                title = generateRandomString(),
                url = generateRandomString()
            )
            list.add(result)
        }
        return list
    }

    fun getTestingSearchListOfSize(size: Int): List<SearchResult> {
        val list = ArrayList<SearchResult>()
        for (i in 0 until size) {
            val result = SearchResult(
                pub_date = generateRandomString(),
                multimedia = emptyList(),
                section_name = generateRandomString(),
                subsection_name = generateRandomString(),
                headline = SearchHeadline(generateRandomString(), generateRandomString()),
                web_url = generateRandomString()
            )
            list.add(result)
        }
        return list
    }

    fun generateRandomString(): String {
        return UUID.randomUUID().toString()
    }
}