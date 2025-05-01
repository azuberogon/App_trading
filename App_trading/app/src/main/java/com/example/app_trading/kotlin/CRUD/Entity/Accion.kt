package com.example.app_trading.kotlin.CRUD.Entity

data class Accion(
    val id: Int,
    val nombre: String,
    val ticker: String,
    val sector: String,
    val pais: String,
    val divisa: String,
    val fechaCreacion: String,
    val fechaUpdate: String,
    val precioAccion: Double,
    val precioCompra: Double,
    val cantidadAcciones: Int,
    val idUser: Int
)
