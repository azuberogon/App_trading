package com.example.app_trading.kotlin.CRUD.Entity



data class User(
    var id: Int? = null,
    var nombre: String? = null,
    var apellido: String? = null,
    var apellido2: String? = null,
    var gmail: String? = null,
    var password: String? = null,
    var imageUrl: String? = null,
    var fechaNaz: String? = null,
    var fechaUpdate: String? = null,
    var dinero: Double? = null,
    var idAccion: Int? = null
) {
    // Constructor sin argumentos requerido por Firebase
    constructor() : this(
        id = null,
        nombre = null,
        apellido = null,
        apellido2 = null,
        gmail = null,
        password = null,
        imageUrl = null,
        fechaNaz = null,
        fechaUpdate = null,
        dinero = null,
        idAccion = null
    )
}