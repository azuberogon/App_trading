package com.example.app_trading.kotlin.CRUD.Entity

data class User(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val apellido2: String,
    val gmail: String,
    val password: String,
    val imageUrl: String,
    val fechaNaz: String,
    val fechaUpdate: String,
    val dinero: Double,
    val idAccion: Int
)
