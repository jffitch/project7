package com.mathgeniusguide.project7.responses.category

data class CategoryResponse(
    val copyright: String,
    val last_updated: String,
    val num_results: Int,
    val results: List<CategoryResult>,
    val section: String,
    val status: String
)