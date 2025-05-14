package com.example.app_trading.kotlin.CRUD.Entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * Clase de datos que representa una noticia recibida de la API.
 * Solo incluye los campos que se mostrarán en el RecyclerView.
 *
 * Implementa [Parcelable] para poder pasar objetos de esta clase entre componentes de Android
 * (aunque en este ejemplo no lo pasaremos directamente, es una buena práctica si planeas hacerlo).
 */
@Parcelize
data class Noticia(
    // @SerializedName se usa si el nombre de la variable Kotlin es diferente del nombre de la clave JSON.
    // En este caso, coinciden, pero lo incluyo como ejemplo.
    @SerializedName("headline")
    val headline: String, // El titular de la noticia.

    @SerializedName("datetime")
    val datetime: Long, // La marca de tiempo Unix de la noticia. Usaremos Long para mayor seguridad.

    @SerializedName("image")
    val image: String, // La URL de la imagen de la noticia.

    @SerializedName("url")
    val url: String // La URL completa de la noticia.
) : Parcelable // Indica que esta clase es parcelable