package com.example.app_trading

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class rm_busquedas : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rm_busquedas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)

        // Configura el adaptador para el AutoCompleteTextView
        val suggestions = mutableListOf<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
        autoCompleteTextView.setAdapter(adapter)

        // Escucha los cambios en el texto
        autoCompleteTextView.addTextChangedListener { text ->
            if (text != null && text.length >= 2) { // Busca a partir de 2 caracteres
                fetchSuggestions(text.toString()) { results ->
                    runOnUiThread {
                        suggestions.clear()
                        suggestions.addAll(results)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    private fun fetchSuggestions(query: String, callback: (List<String>) -> Unit) {
        val client = OkHttpClient()
        val url = "https://api.tiingo.com/tiingo/daily/$query?token=15fd6a8cb82c76c6e845ef46f47956c4319ecaac"
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
                        val jsonArray = JSONArray(responseData)
                        val results = mutableListOf<String>()
                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            results.add(item.getString("ticker")) // Cambia según el campo de la API
                        }
                        callback(results)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(emptyList())
            }
        }.start()
    }
}