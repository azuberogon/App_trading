package com.example.app_trading

import android.os.Bundle
import android.widget.TextView

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.OkHttpClient
import okhttp3.Request

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView) // Asegúrate de tener un TextView en tu XML
        fetchDataFromApi(textView)
    }

    fun fetchDataFromApi(textView: TextView) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.tiingo.com/api/test?token=15fd6a8cb82c76c6e845ef46f47956c4319ecaac")
            .addHeader("Content-Type", "application/json")
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
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



}