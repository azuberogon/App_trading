package com.example.app_trading.kotlin.CRUD.Entity

data class Noticias (
    val category: String,
    val datetime: Long,
    val headline: String,
    val id: Int,
    val image: String,
    val related: String,
    val source: String,
    val summary: String,
    val url: String
)
