package com.mathgeniusguide.project7.responses.search

data class SearchResponse(
    val copyright: String,
    val result: SearchResult,
    val status: String
)