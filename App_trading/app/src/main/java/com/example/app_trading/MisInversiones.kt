package com.example.app_trading

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MisInversiones : AppCompatActivity() {


    // Ejemplo de método onCreate
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_inversiones) // Asegúrate de tener un layout correspondiente


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuNavegacionMisInversiones)

        // Resaltar el elemento actual (MainActivity)
        bottomNavigationView.selectedItemId = R.id.navigation_busqueda

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inversiones -> {
                    startActivity(Intent(this, MisInversiones::class.java))
                    true
                }
                R.id.navigation_noticias -> {
                    startActivity(Intent(this, Noticias::class.java))
                    true
                }
                R.id.navigation_busqueda -> {
                    // Ya estamos en MainActivity, no hacemos nada
                    true
                }
                R.id.navigation_calculadora -> {
                    startActivity(Intent(this, com.example.app_trading.kotlin.Conversor.conversorDeDivisas::class.java))
                    true
                }
                R.id.menuNavegacionAjustes -> {
                    startActivity(Intent(this, Ajustes::class.java))
                    true
                }
                else -> false
            }

        }
    }
}