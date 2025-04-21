package com.example.app_trading.kotlin.InicioSesion.json.Model

data class busquedasEntity(
    val ticker: String,
    val assetType: String,
    val countryCode: String,
    val isActive: Boolean,
    val name: String,
    val openFIGI: String,
    val permaTicker: String
)
