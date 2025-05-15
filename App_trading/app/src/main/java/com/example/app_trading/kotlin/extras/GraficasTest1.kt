package com.example.app_trading.kotlin.extras

            import android.content.Context
            import android.content.Intent
            import android.graphics.Color
            import android.graphics.Paint
            import android.os.Bundle
            import android.widget.Button
            import android.widget.EditText
            import android.widget.TextView
            import android.widget.Toast
            import androidx.appcompat.app.AppCompatActivity
            import com.example.app_trading.R
            import com.example.app_trading.kotlin.Model.StockPrice
            import com.example.app_trading.kotlin.extras.Api.Companion.TOKEN_TINGO
            import com.example.app_trading.kotlin.extras.Api.Companion.TOKEN_TINGO2
            import com.github.mikephil.charting.charts.CandleStickChart
            import com.github.mikephil.charting.components.MarkerView
            import com.github.mikephil.charting.data.CandleData
            import com.github.mikephil.charting.data.CandleDataSet
            import com.github.mikephil.charting.data.CandleEntry
            import com.github.mikephil.charting.data.Entry
            import com.github.mikephil.charting.highlight.Highlight
            import com.github.mikephil.charting.listener.OnChartValueSelectedListener
            import com.github.mikephil.charting.utils.MPPointF
            import com.google.firebase.auth.FirebaseAuth
            import com.google.firebase.firestore.FirebaseFirestore
            import com.google.gson.Gson
            import com.google.gson.reflect.TypeToken
            import okhttp3.OkHttpClient
            import okhttp3.Request

            class GraficasTest1 : AppCompatActivity() {

                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(R.layout.activity_graficas_test1)

                    val candleStickChart = findViewById<CandleStickChart>(R.id.candleStickChart)
                    val textView = findViewById<TextView>(R.id.selectedDataTextView)
                    val inputCantidad = findViewById<EditText>(R.id.inputCantidad1)
                    val btnComprar = findViewById<Button>(R.id.btnComprar1)

                    val ticker = intent.getStringExtra("ticker") ?: return
                    val nombre = intent.getStringExtra("name") ?: ""

                    fetchStockDataFromApi(ticker) { stockData, error ->
                        runOnUiThread {
                            if (error != null) {
                                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                            } else if (stockData != null) {
                                setupCandleStickChart(candleStickChart, stockData)
                                setupChartInteraction(candleStickChart, stockData, textView)
                            }
                        }
                    }

                    btnComprar.setOnClickListener {
                        val cantidadStr = inputCantidad.text.toString()
                        val cantidad = cantidadStr.toIntOrNull()
                        if (cantidad == null || cantidad <= 0) {
                            Toast.makeText(this, "Introduce una cantidad válida", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId == null) {
                            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val accionMap = hashMapOf(
                            "nombre" to nombre,
                            "ticker" to ticker,
                            "cantidadAcciones" to cantidad,
                            "fechaCreacion" to System.currentTimeMillis().toString(),
                            "idUser" to userId
                        )

                        FirebaseFirestore.getInstance().collection("acciones")
                            .add(accionMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Acción comprada y guardada en Firebase", Toast.LENGTH_SHORT).show()
                                // Ir a MisInversiones
                                val intent = Intent(this, com.example.app_trading.MisInversiones::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al guardar en Firebase", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                private fun fetchStockDataFromApi(ticker: String, callback: (List<StockPrice>?, String?) -> Unit) {
                    val client = OkHttpClient()
                    val url = "https://api.tiingo.com/tiingo/daily/$ticker/prices?startDate=2024-01-01&endDate=2024-06-01&token=${TOKEN_TINGO2}"
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
                                    val listType = object : TypeToken<List<StockPrice>>() {}.type
                                    val stockList: List<StockPrice> = Gson().fromJson(responseData, listType)
                                    callback(stockList, null)
                                } else {
                                    callback(null, "Respuesta vacía de la API")
                                }
                            } else {
                                callback(null, "Error en la API: ${response.code}")
                            }
                        } catch (e: Exception) {
                            callback(null, "Error de red: ${e.message}")
                        }
                    }.start()
                }

                private fun setupCandleStickChart(chart: CandleStickChart, data: List<StockPrice>) {
                    val entries = data.mapIndexed { index, stock ->
                        CandleEntry(
                            index.toFloat(),
                            stock.high.toFloat(),
                            stock.low.toFloat(),
                            stock.open.toFloat(),
                            stock.close.toFloat()
                        )
                    }

                    val dataSet = CandleDataSet(entries, "Historial de Precios").apply {
                        decreasingColor = Color.RED
                        increasingColor = Color.GREEN
                        decreasingPaintStyle = Paint.Style.FILL
                        increasingPaintStyle = Paint.Style.FILL
                        shadowColor = Color.DKGRAY
                        shadowWidth = 0.8f
                    }

                    chart.data = CandleData(dataSet)
                    chart.setVisibleXRangeMaximum(20f)
                    chart.isDragEnabled = true
                    chart.setScaleEnabled(true)
                    chart.setPinchZoom(true)
                    chart.invalidate()
                }

                private fun setupChartInteraction(chart: CandleStickChart, data: List<StockPrice>, textView: TextView) {
                    chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            val index = e?.x?.toInt() ?: return
                            if (index in data.indices) {
                                val stock = data[index]
                                textView.text = "Fecha: ${stock.date}\nMáximo: ${stock.high}\nMínimo: ${stock.low}"
                            }
                        }

                        override fun onNothingSelected() {
                            textView.text = "Selecciona una vela para ver detalles"
                        }
                    })
                }

                private fun parseJson(context: Context): List<StockPrice> {
                    val inputStream = context.resources.openRawResource(R.raw.apple)
                    val json = inputStream.bufferedReader().use { it.readText() }
                    val gson = Gson()
                    val type = object : TypeToken<List<StockPrice>>() {}.type
                    return gson.fromJson(json, type)
                }

                class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
                    override fun refreshContent(e: Entry?, highlight: Highlight?) {
                        val candleEntry = e as CandleEntry
                        val data = "Open: ${candleEntry.open}\nClose: ${candleEntry.close}\nHigh: ${candleEntry.high}\nLow: ${candleEntry.low}"

                        super.refreshContent(e, highlight)
                    }

                    override fun getOffset(): MPPointF {
                        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
                    }
                }

                fun calculateSMA(prices: List<StockPrice>, period: Int): List<Entry> {
                    val smaEntries = mutableListOf<Entry>()
                    for (i in prices.indices) {
                        if (i >= period - 1) {
                            val sum = prices.subList(i - period + 1, i + 1).sumOf { it.close }
                            val average = sum / period
                            smaEntries.add(Entry(i.toFloat(), average.toFloat()))
                        }
                    }
                    return smaEntries
                }
            }