package com.example.tempo.data.model

data class ApiResult(
    val articles: List<Article>,
    val totalResults: Int,
    val status: String = "",
    val code: String = "",
    val message: String = "",
)
