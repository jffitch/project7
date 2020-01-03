package com.mathgeniusguide.project7.responses.search

data class SearchResult(
    val headline: SearchHeadline,
    val multimedia: List<SearchMultimedia>,
    val pub_date: String,
    val section_name: String,
    val subsection_name: String?,
    val web_url: String
)