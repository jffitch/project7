package com.mathgeniusguide.project7.responses.search

data class SearchDoc(
    val multimedia: List<SearchMultimedia>,
    val pub_date: String,
    val section_name: String,
    val subsection_name: String,
    val web_url: String
)