package com.example.app_trading.kotlin.Conversor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.Ajustes
import com.example.app_trading.MisInversiones
import com.example.app_trading.Noticias
import com.example.app_trading.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class conversorDeDivisas : AppCompatActivity() {

    private val tasasConversion = mapOf(
        "EUR" to 1.0,
        "USD" to 1.12,
        "MXN" to 18.5,
        "JPY" to 160.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversor_de_divisas)

        val editCantidad = findViewById<EditText>(R.id.editCantidad)
        val spinnerDe = findViewById<Spinner>(R.id.spinnerDe)
        val spinnerA = findViewById<Spinner>(R.id.spinnerA)
        val btnConvertir = findViewById<Button>(R.id.btnConvertir)
        val resultadoTexto = findViewById<TextView>(R.id.resultadoTexto)

        val monedas = tasasConversion.keys.toTypedArray()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuNavegacion)

        // Resaltar el elemento actual (MainActivity)
        bottomNavigationView.selectedItemId = R.id.navigation_busqueda


        spinnerDe.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, monedas)
        spinnerA.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, monedas)

        btnConvertir.setOnClickListener {
            val cantidadTexto = editCantidad.text.toString()
            if (cantidadTexto.isEmpty()) {
                Toast.makeText(this, "Introduce una cantidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadTexto.toDouble()
            val de = spinnerDe.selectedItem.toString()
            val a = spinnerA.selectedItem.toString()

            val resultado = convertirDivisa(cantidad, de, a)
            resultadoTexto.text = "Resultado: %.2f $a".format(resultado)
        }






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
                R.id.navigation_Ajustes -> {
                    startActivity(Intent(this, Ajustes::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun convertirDivisa(cantidad: Double, de: String, a: String): Double {
        val tasaDe = tasasConversion[de] ?: 1.0
        val tasaA = tasasConversion[a] ?: 1.0
        return cantidad / tasaDe * tasaA
    }
}