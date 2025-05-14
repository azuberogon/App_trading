package com.example.app_trading

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.kotlin.Adapter.NoticiasAdapter
import com.example.app_trading.kotlin.CRUD.service.FinnhubService
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Noticias : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)

        // Configuración del BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuNavegacionNoticias)
        bottomNavigationView.selectedItemId = R.id.navigation_noticias
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inversiones -> {
                    startActivity(Intent(this, MisInversiones::class.java))
                    true
                }
                R.id.navigation_busqueda -> {
                    startActivity(Intent(this, MainActivity::class.java))
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

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewNoticias)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Llamada a la API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FinnhubService::class.java)
        val call = service.getGeneralNews(token = "TU_API_KEY")

        call.enqueue(object : Callback<List<Noticias>> {
            override fun onResponse(call: Call<List<Noticias>>, response: Response<List<Noticias>>) {
                if (response.isSuccessful) {
                    val newsList = response.body() ?: emptyList()
                    val noticiasAdapter = NoticiasAdapter(newsList) { noticia ->
                        try {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(noticia.url))
                            startActivity(browserIntent)
                        } catch (e: Exception) {
                            Toast.makeText(this@Noticias, "No se pudo abrir la URL", Toast.LENGTH_SHORT).show()
                        }
                    }
                    recyclerView.adapter = noticiasAdapter
                } else {
                    Log.e("Noticias", "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Noticias>>, t: Throwable) {
                Log.e("Noticias", "Fallo de conexión: ${t.message}")
            }
        })
    }
}