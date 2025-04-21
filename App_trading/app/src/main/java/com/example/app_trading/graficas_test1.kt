package com.example.app_trading

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.graphics.Color

import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

class graficas_test1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graficas_test1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtén la referencia al CandleStickChart desde el layout
        val candleStickChart = findViewById<CandleStickChart>(R.id.candleStickChart)

        // Datos de ejemplo para la gráfica
        val entries = listOf(
            CandleEntry(0f, 220f, 200f, 210f, 205f), // (x, high, low, open, close)
            CandleEntry(1f, 230f, 210f, 220f, 215f),
            CandleEntry(2f, 240f, 220f, 230f, 225f),
            CandleEntry(3f, 250f, 230f, 240f, 235f),
            CandleEntry(4f, 260f, 240f, 250f, 245f)
        )

        // Configura el DataSet
        val dataSet = CandleDataSet(entries, "Demo CandleStickChart")
        dataSet.color = Color.rgb(80, 80, 80)
        dataSet.shadowColor = Color.DKGRAY
        dataSet.shadowWidth = 0.8f
        dataSet.decreasingColor = Color.RED
        dataSet.decreasingPaintStyle = android.graphics.Paint.Style.FILL
        dataSet.increasingColor = Color.GREEN
        dataSet.increasingPaintStyle = android.graphics.Paint.Style.FILL
        dataSet.neutralColor = Color.BLUE

        // Configura los datos y asigna al gráfico
        val data = CandleData(dataSet)
        candleStickChart.data = data

        // Opcional: Personaliza el gráfico
        candleStickChart.description.isEnabled = false
        candleStickChart.setPinchZoom(true)
        candleStickChart.setDrawGridBackground(false)

        // Refresca el gráfico
        candleStickChart.invalidate()

    }
}