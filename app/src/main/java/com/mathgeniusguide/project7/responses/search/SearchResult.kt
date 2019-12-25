package com.mathgeniusguide.project7.responses.search

data class SearchResult(
    val _id: String,
    val `abstract`: String,
    val document_type: String,
    val headline: SearchHeadline,
    val keywords: List<SearchKeyword>,
    val lead_paragraph: String,
    val multimedia: List<SearchMultimedia>,
    val news_desk: String,
    val print_page: String,
    val pub_date: String,
    val section_name: String,
    val snippet: String,
    val source: String,
    val subsection_name: String?,
    val type_of_material: String,
    val uri: String,
    val web_url: String,
    val word_count: Int
)