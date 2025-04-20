package com.example.app_trading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.kotlin.InicioSesion.json.Adapter.CustomAdapter
import com.example.app_trading.kotlin.InicioSesion.json.Adapter.StockAdapter
import com.example.app_trading.kotlin.InicioSesion.json.Model.StockPrice
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchDataFromApi { stockList ->
            runOnUiThread {
                recyclerView.adapter = CustomAdapter(stockList)
            }
        }
    }

    /**

     * Obtiene datos históricos de precios de acciones para Apple (AAPL) desde la API de Tiingo.
     *
     * Esta función realiza una solicitud de red asíncrona a la API de Tiingo para recuperar
     * los precios históricos diarios de las acciones de AAPL. Los datos se obtienen para el período que comienza
     * desde '2019-01-02'. Se espera que la respuesta de la API sea un array JSON de objetos `StockPrice`.
     *
     * La función utiliza un callback para entregar los datos obtenidos una vez que estén disponibles.
     *
     * @param callback Una función lambda que acepta una `List<StockPrice>` como parámetro.
     *                 Este callback se invocará en un hilo de fondo con los datos de precios
     *                 de acciones obtenidos, o una lista vacía si ocurrió un error o los datos son nulos.
     *
     * @throws Exception Si ocurre algún error de red o de análisis (parsing) durante el proceso. Las excepciones se capturan internamente e imprimen en la consola.
     *
     * Nota:
     * - Esta función realiza operaciones de red en un hilo de fondo para evitar el bloqueo del hilo principal.
     * - El token de la API de Tiingo está codificado directamente en este ejemplo. En un entorno de producción, debe almacenarse de forma segura y recuperarse adecuadamente.
     * - El punto final (endpoint) de la API y los parámetros también están codificados directamente. Para mayor flexibilidad, es posible que desees parametrizar estos valores.
     * - El manejo de errores es básico en este ejemplo. En una aplicación real, podría mejorarse añadiendo un segundo parámetro al callback para pasar información sobre el error.
     */
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


