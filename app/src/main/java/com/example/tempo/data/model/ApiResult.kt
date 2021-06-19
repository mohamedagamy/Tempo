package com.example.tempo.data.model

data class ApiResult(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int,
    val code: String,
    val message: String,
)