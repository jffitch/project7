package com.mathgeniusguide.project7.responses.popular

import com.squareup.moshi.Json

data class PopularMedia(
    val approved_for_syndication: Int,
    val caption: String,
    val copyright: String,
    @field:Json(name = "media-metadata")
    val mediaMetadata: List<PopularMediaMetadata>,
    val subtype: String,
    val type: String
)