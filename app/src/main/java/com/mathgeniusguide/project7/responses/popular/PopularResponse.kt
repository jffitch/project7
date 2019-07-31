package com.mathgeniusguide.project7.responses.popular

data class PopularResponse(
    val copyright: String,
    val num_results: Int,
    val results: List<PopularResult>,
    val status: String
)