package com.mathgeniusguide.project7.responses.popular

data class PopularResponse(
    val results: List<PopularResult>,
    val status: String
)