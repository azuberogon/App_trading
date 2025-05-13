package com.example.app_trading.kotlin.extras

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.R
import com.example.app_trading.kotlin.Model.StockPrice
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GraficasTest1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graficas_test1)

        val candleStickChart = findViewById<CandleStickChart>(R.id.candleStickChart)
        candleStickChart.setScaleEnabled(true) // Habilita el zoom
        candleStickChart.setDragEnabled(true) // Habilita el desplazamiento
        candleStickChart.setPinchZoom(true) // Permite zoom con dos dedos
        val textView = findViewById<TextView>(R.id.selectedDataTextView)

        val stockData = parseJson(this)
        setupCandleStickChart(candleStickChart, stockData)
        setupChartInteraction(candleStickChart, stockData, textView)

//

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
        val inputStream = context.resources.openRawResource(R.raw.apple) // Accede al archivo en res/raw
        val json = inputStream.bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<List<StockPrice>>() {}.type
        return gson.fromJson(json, type)
    }

    class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            val candleEntry = e as CandleEntry
            val data = "Open: ${candleEntry.open}\nClose: ${candleEntry.close}\nHigh: ${candleEntry.high}\nLow: ${candleEntry.low}"
            findViewById<TextView>(R.id.markerTextView).text = data
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