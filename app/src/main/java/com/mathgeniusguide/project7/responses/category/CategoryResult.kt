package com.mathgeniusguide.project7.responses.category

data class CategoryResult(
    val `abstract`: String,
    val byline: String,
    val created_date: String,
    val item_type: String,
    val kicker: String,
    val material_type_facet: String,
    val multimedia: List<CategoryMultimedia>,
    val published_date: String,
    val section: String,
    val short_url: String,
    val subsection: String?,
    val title: String,
    val updated_date: String,
    val url: String
)