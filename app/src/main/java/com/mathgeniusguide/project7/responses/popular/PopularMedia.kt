package com.mathgeniusguide.project7.responses.popular

data class PopularMedia(
    val approved_for_syndication: Int,
    val caption: String,
    val copyright: String,
    val mediaMetadata: List<PopularMediaMetadata>,
    val subtype: String,
    val type: String
)