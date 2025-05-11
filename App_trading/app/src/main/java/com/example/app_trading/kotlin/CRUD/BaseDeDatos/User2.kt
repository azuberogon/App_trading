package com.example.app_trading.kotlin.CRUD.BaseDeDatos

data class User2 (

    var id: String ?,
    val userId: String,
    var nombre: String,
    var email: String,
    var contrasena: String,



){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "id" to (id ?: ""),
            "nombre" to this.nombre,
            "email" to this.email,
            "contrasena" to this.contrasena,


        )
    }
}