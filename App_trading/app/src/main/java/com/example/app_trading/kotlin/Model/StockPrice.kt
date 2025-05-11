package com.example.app_trading.kotlin.Model
//clase para las acciones que se mostraran en las graficas
/*data class StockPrice(
    val date: String,
    val close: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val volume: Int
)*/
data class StockPrice(
    val date: String,
    val close: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val volume: Long,
    val adjClose: Double,
    val adjHigh: Double,
    val adjLow: Double,
    val adjOpen: Double,
    val adjVolume: Long,
    val divCash: Double,
    val splitFactor: Double
)

//da fallos solucionar