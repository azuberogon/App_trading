package com.example.app_trading.json.Model

    data class StockPrice(
    val date: String,
    val close: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val volume: Int
)

//da fallos solucionar 