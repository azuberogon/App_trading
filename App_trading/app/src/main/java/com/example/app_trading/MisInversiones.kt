package com.example.app_trading

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.kotlin.Adapter.InversionesAdapter

import com.example.app_trading.kotlin.CRUD.BaseDeDatos.DatabaseHelper
import com.example.app_trading.kotlin.CRUD.Entity.Inversion
import com.example.app_trading.kotlin.extras.Noticias
import com.google.android.material.bottomnavigation.BottomNavigationView

class MisInversiones : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_inversiones)

        val dbHelper = DatabaseHelper(this)
        val listaInversiones: List<Inversion> = dbHelper.getAllInversiones()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewInversiones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = InversionesAdapter(listaInversiones) { idAccion ->
            val intent = Intent(this, Ventas::class.java)
            intent.putExtra("idAccion", idAccion)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuNavegacionMisInversiones)
        bottomNavigationView.selectedItemId = R.id.inversiones

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inversiones -> true
                R.id.navigation_noticias -> {
                    startActivity(Intent(this, Noticias::class.java))
                    finish()
                    true
                }
                R.id.navigation_busqueda -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_calculadora -> {
                    startActivity(Intent(this, com.example.app_trading.kotlin.Conversor.conversorDeDivisas::class.java))
                    finish()
                    true
                }
                R.id.menuNavegacionAjustes -> {
                    startActivity(Intent(this, Ajustes::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }


        //dbHelper.poblarInversionesDemo() // Pobla si está vacío

    }

    override fun onResume() {
        super.onResume()
        val dbHelper = DatabaseHelper(this)
        val listaInversiones = dbHelper.getAllInversiones().filter { it.cantidad > 0 }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewInversiones)
        (recyclerView.adapter as? InversionesAdapter)?.updateData(listaInversiones)
    }
    fun InversionesAdapter.updateData(newData: List<Inversion>) {
        this.inversiones = newData
        notifyDataSetChanged()
    }
}