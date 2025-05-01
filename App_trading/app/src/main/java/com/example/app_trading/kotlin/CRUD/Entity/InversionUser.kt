package com.example.app_trading.kotlin.CRUD.Entity

import java.util.Date

data class InversionUser(
    val id: Int,
    val nombreAccion: String,
    val cantidad: Int,
    val valorAccionActual: Double,
    val valorAccionCompra: Double,
    val fechaCompra: Date,

)
/*
* // Para N compras:
val totalCantidad = compras.sumOf { it.cantidad }
val totalInvertido = compras.sumOf { it.cantidad * it.valorAccionCompra }
val promedioCompra = totalInvertido / totalCantidad

* */