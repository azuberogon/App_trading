package com.example.app_trading.kotlin.FireBase

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class Firebase : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("TradingApp", "Initializing Firebase...")
        FirebaseApp.initializeApp(this)
        Log.d("TradingApp", "Firebase initialized.")
    }
}