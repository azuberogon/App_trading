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

class SplashScreenInicioDesesion : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash_screen_inicio_desesion)

            // Referencia al ImageView con findViewById
            val gifImageView = findViewById<ImageView>(R.id.gifImageView)

            // Cargar el GIF en el ImageView
            Glide.with(this)
                .asGif()
                .load(R.drawable.ventana_carga) // Asegúrate de tener el gif en res/drawable
                .into(gifImageView)

            // Esperar 3 segundos y lanzar la MainActivity
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 6000)
        }
}