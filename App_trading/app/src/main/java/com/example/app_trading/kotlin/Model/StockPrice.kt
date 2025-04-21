package com.example.app_trading.kotlin.Model
//clase para las acciones que se mostraran en las graficas
data class StockPrice(
    val date: String,
    val close: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val volume: Int
)

//da fallos solucionar