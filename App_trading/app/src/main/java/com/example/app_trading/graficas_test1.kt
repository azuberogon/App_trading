package com.example.app_trading

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

class graficas_test1 : AppCompatActivity() {
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graficas_test1)

        val name = intent.getStringExtra("name") ?: "Sin nombre"
        val ticker = intent.getStringExtra("ticker") ?: ""

        // Configurar el título
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        titleTextView.text = name

        // Cargar datos desde el JSON estático
        loadStaticJsonData()
        // Realizar la llamada a la API
        //fetchCandleData(ticker)


    }
/*
    private fun fetchCandleData(ticker: String) {
        val client = OkHttpClient()
        val url = "https://api.tiingo.com/tiingo/daily/$ticker/prices?startDate=2012-1-1&endDate=2016-1-1&format=json&resampleFreq=monthly&token=${Api.TOKEN}"
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
                        val entries = mutableListOf<CandleEntry>()

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val date = i.toFloat()
                            val high = jsonObject.getDouble("high").toFloat()
                            val low = jsonObject.getDouble("low").toFloat()
                            val open = jsonObject.getDouble("open").toFloat()
                            val close = jsonObject.getDouble("close").toFloat()
                            entries.add(CandleEntry(date, high, low, open, close))
                        }

                        runOnUiThread {
                            if (entries.isEmpty()) {
                                val titleTextView = findViewById<TextView>(R.id.titleTextView)
                                titleTextView.text = "No hay datos disponibles para el ticker: $ticker"
                            } else {
                                setupCandleChart(entries)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
*/


    private fun setupTradingChart(entries: List<CandleEntry>, volumeEntries: List<BarEntry>) {
        val combinedChart = findViewById<CombinedChart>(R.id.combinedChart)

        // Configurar el dataset de velas
        val candleDataSet = CandleDataSet(entries, "Historial de Precios")
        candleDataSet.color = android.graphics.Color.rgb(80, 80, 80)
        candleDataSet.shadowColor = android.graphics.Color.DKGRAY
        candleDataSet.shadowWidth = 1.5f // Grosor de las líneas de sombra
        candleDataSet.decreasingColor = android.graphics.Color.RED
        candleDataSet.increasingColor = android.graphics.Color.GREEN
        candleDataSet.decreasingPaintStyle = android.graphics.Paint.Style.FILL
        candleDataSet.increasingPaintStyle = android.graphics.Paint.Style.FILL
        candleDataSet.barSpace = 0.1f // Reduce el espacio entre velas

        // Configurar el dataset de volumen
        // Configurar el dataset de volumen
        val barDataSet = BarDataSet(volumeEntries, "Volumen")
        barDataSet.color = android.graphics.Color.BLUE
        barDataSet.axisDependency = YAxis.AxisDependency.RIGHT

// Crear los datos de barras
        val barData = BarData(barDataSet)
        barData.barWidth = 0.9f // Configurar el ancho de las barras

// Combinar los datos
        val candleData = CandleData(candleDataSet)
        val combinedData = CombinedData()
        combinedData.setData(candleData)
        combinedData.setData(barData)

        // Configurar el gráfico combinado
        combinedChart.data = combinedData
        combinedChart.setDrawGridBackground(false)
        combinedChart.setDrawBarShadow(false)
        combinedChart.isHighlightPerDragEnabled = true
        combinedChart.setScaleEnabled(true)
        combinedChart.setPinchZoom(true)

        // Configurar el eje X
        val xAxis = combinedChart.xAxis
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.textSize = 14f // Tamaño del texto del eje X
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "Día ${value.toInt() + 1}" // Ajusta según tus datos
            }
        }

        // Configurar el eje Y izquierdo (para precios)
        val yAxisLeft = combinedChart.axisLeft
        yAxisLeft.setDrawGridLines(true)
        yAxisLeft.textSize = 14f // Tamaño del texto del eje Y izquierdo

        // Configurar el eje Y derecho (para volumen)
        val yAxisRight = combinedChart.axisRight
        yAxisRight.setDrawGridLines(false)
        yAxisRight.textSize = 14f // Tamaño del texto del eje Y derecho
        yAxisRight.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${(value / 1000).toInt()}k"
            }
        }

        // Configurar la leyenda
        val legend = combinedChart.legend
        legend.textSize = 16f // Tamaño del texto de la leyenda

        // Ajustar el zoom inicial
        combinedChart.zoom(2f, 1f, 0f, 0f) // Zoom horizontal (2x) y vertical (1x)

        combinedChart.invalidate() // Refrescar el gráfico
    }
//    private fun setupTradingChart(entries: List<CandleEntry>, volumeEntries: List<BarEntry>) {
//        val combinedChart = findViewById<CombinedChart>(R.id.combinedChart)
//
//        // Configurar el dataset de velas
//        val candleDataSet = CandleDataSet(entries, "Historial de Precios")
//        candleDataSet.color = android.graphics.Color.rgb(80, 80, 80)
//        candleDataSet.shadowColor = android.graphics.Color.DKGRAY
//        candleDataSet.shadowWidth = 0.8f
//        candleDataSet.decreasingColor = android.graphics.Color.RED
//        candleDataSet.increasingColor = android.graphics.Color.GREEN
//        candleDataSet.decreasingPaintStyle = android.graphics.Paint.Style.FILL
//        candleDataSet.increasingPaintStyle = android.graphics.Paint.Style.FILL
//
//        // Configurar el dataset de volumen
//        val barDataSet = BarDataSet(volumeEntries, "Volumen")
//        barDataSet.color = android.graphics.Color.BLUE
//        barDataSet.axisDependency = YAxis.AxisDependency.RIGHT
//
//        // Combinar los datos
//        val candleData = CandleData(candleDataSet)
//        val barData = BarData(barDataSet)
//        val combinedData = CombinedData()
//        combinedData.setData(candleData)
//        combinedData.setData(barData)
//
//        // Configurar el gráfico combinado
//        combinedChart.data = combinedData
//        combinedChart.setDrawGridBackground(false)
//        combinedChart.setDrawBarShadow(false)
//        combinedChart.isHighlightPerDragEnabled = true
//        combinedChart.setScaleEnabled(true)
//        combinedChart.setPinchZoom(true)
//
//        // Configurar el eje X
//        val xAxis = combinedChart.xAxis
//        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
//        xAxis.granularity = 1f
//        xAxis.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return "Día ${value.toInt() + 1}" // Ajusta según tus datos
//            }
//        }
//
//        // Configurar el eje Y izquierdo (para precios)
//        val yAxisLeft = combinedChart.axisLeft
//        yAxisLeft.setDrawGridLines(true)
//
//        // Configurar el eje Y derecho (para volumen)
//        val yAxisRight = combinedChart.axisRight
//        yAxisRight.setDrawGridLines(false)
//        yAxisRight.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return "${(value / 1000).toInt()}k"
//            }
//        }
//
//        combinedChart.invalidate() // Refrescar el gráfico
//    }

    private fun loadStaticJsonData() {
        try {
            // Leer el archivo JSON desde res/raw
            val inputStream = resources.openRawResource(R.raw.apple)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = bufferedReader.use { it.readText() }

            // Parsear el JSON
            val jsonArray = JSONArray(jsonString)
            val entries = mutableListOf<CandleEntry>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val date = i.toFloat()
                val high = jsonObject.getDouble("high").toFloat()
                val low = jsonObject.getDouble("low").toFloat()
                val open = jsonObject.getDouble("open").toFloat()
                val close = jsonObject.getDouble("close").toFloat()
                entries.add(CandleEntry(date, high, low, open, close))
            }

            // Configurar la gráfica
            runOnUiThread {
                if (entries.isEmpty()) {
                    val titleTextView = findViewById<TextView>(R.id.titleTextView)
                    titleTextView.text = "No hay datos disponibles en el JSON"
                } else {
                    setupCandleChart(entries)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun setupCandleChart(entries: List<CandleEntry>) {
        val candleStickChart = findViewById<CandleStickChart>(R.id.candleStickChart)

        // Configurar el dataset de velas
        val candleDataSet = CandleDataSet(entries, "Historial de Precios")
        candleDataSet.color = android.graphics.Color.rgb(80, 80, 80)
        candleDataSet.shadowColor = android.graphics.Color.DKGRAY
        candleDataSet.shadowWidth = 0.8f
        candleDataSet.decreasingColor = android.graphics.Color.RED
        candleDataSet.increasingColor = android.graphics.Color.GREEN
        candleDataSet.decreasingPaintStyle = android.graphics.Paint.Style.FILL
        candleDataSet.increasingPaintStyle = android.graphics.Paint.Style.FILL

        // Crear los datos del gráfico
        val candleData = CandleData(candleDataSet)
        candleStickChart.data = candleData

        // Configurar el gráfico
        candleStickChart.setDrawGridBackground(false)
        candleStickChart.setPinchZoom(true)
        candleStickChart.setScaleEnabled(true)
        candleStickChart.isHighlightPerDragEnabled = true

        // Configurar el eje X
        val xAxis = candleStickChart.xAxis
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "Día ${value.toInt() + 1}" // Ajusta según tus datos
            }
        }

        // Configurar el eje Y
        val yAxisLeft = candleStickChart.axisLeft
        yAxisLeft.setDrawGridLines(true)

        val yAxisRight = candleStickChart.axisRight
        yAxisRight.setDrawGridLines(false)

        // Refrescar el gráfico
        candleStickChart.invalidate()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graficas_test1)

        val candleStickChart = findViewById<CandleStickChart>(R.id.candleStickChart)
        val btnComprar = findViewById<Button>(R.id.btnComprar)
        val btnVender = findViewById<Button>(R.id.btnVender)
        val inputComprar = findViewById<EditText>(R.id.inputComprar)

        // Configurar la gráfica
        setupCandleStickChart(candleStickChart)

        // Mostrar input al pulsar "Comprar"
        btnComprar.setOnClickListener {
            inputComprar.visibility = if (inputComprar.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        // Mostrar diálogo al pulsar "Vender"
        btnVender.setOnClickListener {
            showSellDialog()
        }
    }

    private fun setupCandleStickChart(chart: CandleStickChart) {
        val entries = listOf(
            CandleEntry(0f, 200f, 180f, 190f, 195f),
            CandleEntry(1f, 210f, 190f, 200f, 205f),
            CandleEntry(2f, 220f, 200f, 210f, 215f)
        )

        val dataSet = CandleDataSet(entries, "Historial de Precios")
        dataSet.decreasingColor = android.graphics.Color.RED
        dataSet.increasingColor = android.graphics.Color.GREEN
        dataSet.decreasingPaintStyle = android.graphics.Paint.Style.FILL
        dataSet.increasingPaintStyle = android.graphics.Paint.Style.FILL

        chart.data = CandleData(dataSet)
        chart.setPinchZoom(true)
        chart.isHighlightPerDragEnabled = true
        chart.invalidate()
    }

    private fun showSellDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_dialog_sell, null)
        val inputSell = dialogView.findViewById<EditText>(R.id.inputSell)

        AlertDialog.Builder(this)
            .setTitle("Vender Acciones")
            .setView(dialogView)
            .setPositiveButton("Vender") { _, _ ->
                val cantidad = inputSell.text.toString()
                Toast.makeText(this, "Vendiste $cantidad acciones", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}