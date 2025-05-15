package com.example.app_trading.kotlin.CRUD.Entity



    data class Accion(
        var id: Int? = null,
        var nombre: String? = null,
        var ticker: String? = null,
        var sector: String? = null,
        var pais: String? = null,
        var divisa: String? = null,
        var fechaCreacion: String? = null,
        var fechaUpdate: String? = null,
        var precioAccion: Double? = null,
        var precioCompra: Double? = null,
        var cantidadAcciones: Int? = null,
        var idUser: String? = null
    )