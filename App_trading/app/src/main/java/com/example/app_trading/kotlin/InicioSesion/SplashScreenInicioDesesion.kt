package com.example.app_trading.kotlin.InicioSesion

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app_trading.MainActivity
import com.example.app_trading.R


/**
 * Esta actividad implementa una pantalla de bienvenida (Splash Screen).
 * Muestra un GIF durante un período de tiempo determinado y luego
 * navega automáticamente a la [MainActivity]. Es útil para cargar
 * recursos o realizar inicializaciones antes de que el usuario acceda
 * a la funcionalidad principal de la aplicación.
 */
class SplashScreenInicioDesesion : AppCompatActivity() {

    /**
     * Método onCreate de la actividad. Se llama cuando se crea la actividad.
     * Configura la interfaz de usuario, carga un GIF animado y establece un retraso
     * para navegar a la siguiente actividad.
     *
     * @param savedInstanceState Estado de instancia previamente guardado, si lo hay.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establecer el diseño de la actividad
        setContentView(R.layout.activity_splash_screen_inicio_desesion)

        // Referencia al ImageView donde se mostrará el GIF utilizando findViewById
        val gifImageView = findViewById<ImageView>(R.id.gifImageView)

        // Cargar el GIF en el ImageView utilizando la biblioteca Glide
        Glide.with(this)
            .asGif() // Especifica que se va a cargar un GIF
            .load(R.drawable.ventana_carga) // Carga el recurso GIF desde res/drawable
            .into(gifImageView) // Muestra el GIF en el ImageView

        // Utilizar un Handler para introducir un retraso antes de iniciar la siguiente actividad
        // Looper.getMainLooper() asegura que la tarea se ejecute en el hilo principal de UI
        Handler(Looper.getMainLooper()).postDelayed({
            // Crear un Intent para iniciar la MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            // Finalizar esta actividad para que el usuario no pueda volver a ella con el botón "atrás"
            finish()
        }, 6000) // El retraso es de 6000 milisegundos (6 segundos)
    }
}