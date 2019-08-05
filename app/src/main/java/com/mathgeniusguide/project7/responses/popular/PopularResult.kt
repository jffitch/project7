package com.mathgeniusguide.project7.responses.popular

data class PopularResult(
    val `abstract`: String,
    val adx_keywords: String,
    val asset_id: Long,
    val byline: String,
    val column: String,
    val id: Long,
    val media: List<PopularMedia>,
    val published_date: String,
    val section: String,
    val source: String,
    val title: String,
    val type: String,
    val url: String,
    val views: Int
)