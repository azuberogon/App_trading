package com.example.app_trading.kotlin.Model
//esto es para el rv que bsuca las empresas que cotizane bolsa
data class busquedasEntity(
    val ticker: String,
    val assetType: String,
    val countryCode: String,
    val isActive: Boolean,
    val name: String,
    val openFIGI: String,
    val permaTicker: String
)
