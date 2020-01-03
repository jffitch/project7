package com.mathgeniusguide.project7.responses.category

data class CategoryResult(
    val multimedia: List<CategoryMultimedia>,
    val created_date: String,
    val section: String,
    val subsection: String?,
    val title: String,
    val url: String
)