package com.example.app_trading

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.json.Adapter.StockAdapter
import com.example.app_trading.json.Model.StockPrice
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileWriter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    /*override fun onCreate(savedInstanceState: Bundle?) { // se sobre escribe la actividad actual
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }*/
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView) // Asegúrate de tener un TextView en tu XML
        fetchDataFromApi(textView)
    }*/

    /*fun fetchDataFromApi(textView: TextView) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2019-01-02&token=15fd6a8cb82c76c6e845ef46f47956c4319ecaac")
            .addHeader("Content-Type", "application/json")
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) { //Verifica si la respuesta fue exitosa (código de estado HTTP 200).
                    val responseData = response.body?.string()// body se usa para las respuestas de http para poder editar le texto en este caso un json
                    // Guarda el JSON en un archivo
                    if (responseData != null) {
                        saveJsonToFile(responseData)
                    }

                    runOnUiThread {
                        textView.text = responseData // Actualiza el texto en el hilo principal
                    }
                } else {
                    runOnUiThread {
                        textView.text = "Error: ${response.code}"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun saveJsonToFile(jsonData: String) {
        try {
            // Obtener la ruta a la carpeta 'sampledata' dentro del proyecto
            val folder = File(applicationContext.filesDir.parentFile, "resources")
            if (!folder.exists()) {

                folder.mkdir() // Crea la carpeta si no existe
            }

            // Archivo dentro de la carpeta
            val file = File(folder, "data.json")
            val writer = FileWriter(file)
            println(file.absolutePath)
            writer.use {
                it.write(jsonData) // Escribe el JSON en el archivo
            }

            runOnUiThread {
                Toast.makeText(this, "Archivo guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Error al guardar el archivo", Toast.LENGTH_SHORT).show()
            }
        }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchDataFromApi { stockList ->
            runOnUiThread {
                recyclerView.adapter = StockAdapter(stockList)
            }
        }
    }


    /*fun fetchDataFromApi(textView: TextView) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2019-01-02&token=15fd6a8cb82c76c6e845ef46f47956c4319ecaac")
            .addHeader("Content-Type", "application/json")
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        // Parseamos el JSON a una lista de StockPrice
                        val listType = object : TypeToken<List<StockPrice>>() {}.type
                        val stockPrices: List<StockPrice> = Gson().fromJson(responseData, listType)

                        runOnUiThread {
                            textView.text = "Último precio: ${stockPrices[0].close}"
                        }
                    }
                } else {
                    runOnUiThread {
                        textView.text = "Error: ${response.code}"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }*/
    fun fetchDataFromApi(callback: (List<StockPrice>) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2019-01-02&token=15fd6a8cb82c76c6e845ef46f47956c4319ecaac")
            .addHeader("Content-Type", "application/json")
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        val listType = object : TypeToken<List<StockPrice>>() {}.type
                        val stockPrices: List<StockPrice> = Gson().fromJson(responseData, listType)
                        callback(stockPrices)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

}


