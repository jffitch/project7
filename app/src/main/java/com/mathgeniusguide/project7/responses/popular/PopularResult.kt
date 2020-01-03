package com.mathgeniusguide.project7.responses.popular

data class PopularResult(
    val media: List<PopularMedia>,
    val published_date: String,
    val section: String,
    val title: String,
    val url: String
)