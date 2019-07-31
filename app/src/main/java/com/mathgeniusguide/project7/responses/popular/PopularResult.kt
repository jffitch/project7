package com.mathgeniusguide.project7.responses.popular

data class PopularResult(
    val `abstract`: String,
    val adx_keywords: String,
    val asset_id: Long,
    val byline: String,
    val column: String,
    val des_facet: List<String>,
    val geo_facet: String,
    val id: Long,
    val media: List<PopularMedia>,
    val org_facet: List<String>,
    val per_facet: List<String>,
    val published_date: String,
    val section: String,
    val source: String,
    val title: String,
    val type: String,
    val url: String,
    val views: Int
)