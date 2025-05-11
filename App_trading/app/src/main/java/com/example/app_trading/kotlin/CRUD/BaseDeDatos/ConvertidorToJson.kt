package com.example.app_trading.kotlin.CRUD.BaseDeDatos
import com.example.app_trading.kotlin.CRUD.Entity.Accion
import com.example.app_trading.kotlin.CRUD.Entity.User
import com.google.gson.Gson
class ConvertidorToJson {

    fun userToJson(user: User): String {
        val gson = Gson()
        return gson.toJson(user)
    }

    fun accionToJson(accion: Accion): String {
        val gson = Gson()
        return gson.toJson(accion)
    }
}