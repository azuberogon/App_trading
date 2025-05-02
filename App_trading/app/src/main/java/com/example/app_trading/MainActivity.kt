package com.example.app_trading

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.kotlin.Adapter.CustomAdapter
import com.example.app_trading.kotlin.Model.busquedasEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import android.os.Handler
import android.os.Looper
class MainActivity : AppCompatActivity() {
    private val fullList = mutableListOf<busquedasEntity>() // Lista completa de datos
    private lateinit var recyclerAdapter: CustomAdapter
    private val handler = Handler(Looper.getMainLooper()) // Handler para debounce
    private var searchRunnable: Runnable? = null // Runnable para la búsqueda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = CustomAdapter(fullList) { selectedItem ->
            val intent = Intent(this, graficas_test1::class.java)
            intent.putExtra("name", selectedItem.name)
            intent.putExtra("ticker", selectedItem.ticker)
            startActivity(intent)
        }
        recyclerView.adapter = recyclerAdapter

        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        // Escucha los cambios en el texto del AutoCompleteTextView con debounce
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchRunnable?.let { handler.removeCallbacks(it) } // Cancela el runnable anterior
                searchRunnable = Runnable {
                    if (!s.isNullOrEmpty() && s.length >= 2) {
                        fetchDataFromApi(s.toString()) { busquedasList, error ->
                            runOnUiThread {
                                if (error != null) {
                                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                                } else {
                                    fullList.clear()
                                    fullList.addAll(busquedasList)
                                    recyclerAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
                handler.postDelayed(searchRunnable!!, 500) // Espera 500ms antes de ejecutar
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchDataFromApi(query: String, callback: (List<busquedasEntity>, String?) -> Unit) {
        val client = OkHttpClient()
        val url = "https://api.tiingo.com/tiingo/utilities/search?query=$query&token=${Api.TOKEN}"
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        val listType = object : TypeToken<List<busquedasEntity>>() {}.type
                        val busquedasList: List<busquedasEntity> = Gson().fromJson(responseData, listType)
                        callback(busquedasList, null) // Sin errores
                    } else {
                        callback(emptyList(), "Respuesta vacía de la API")
                    }
                } else {
                    callback(emptyList(), "Error en la API: ${response.code}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(emptyList(), "Error de red: ${e.message}")
            }
        }.start()
    }
}