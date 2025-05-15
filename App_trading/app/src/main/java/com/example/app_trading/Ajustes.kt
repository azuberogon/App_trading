package com.example.app_trading

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.kotlin.InicioSesion.InicioCuenta.Inicio_de_sesion
import com.example.app_trading.kotlin.extras.Noticias
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Ajustes : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    //Firebase.auth.signOut()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajustes)

        auth = FirebaseAuth.getInstance()
        val switchModoOscuro = findViewById<Switch>(R.id.switchModoOscuro)
        val switchNotificaciones = findViewById<Switch>(R.id.switchNotificaciones)
        val spinnerIdioma = findViewById<Spinner>(R.id.spinnerIdioma)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuNavegacionAjustes)

        btnCerrarSesion.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Confirmar cierre de sesión")
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")

            builder.setPositiveButton("Sí") { _, _ ->
                auth.signOut()
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Inicio_de_sesion::class.java))
                finish()
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }


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
                    startActivity(
                        Intent(
                            this,
                            com.example.app_trading.kotlin.Conversor.conversorDeDivisas::class.java
                        )
                    )
                    true
                }

                R.id.navigation_Ajustes -> {
                    startActivity(Intent(this, Ajustes::class.java))
                    true
                }

                else -> false
            }
        }
    }
}