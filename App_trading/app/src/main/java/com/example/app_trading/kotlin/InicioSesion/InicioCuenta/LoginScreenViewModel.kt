package com.example.app_trading.kotlin.InicioSesion.InicioCuenta

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    val loading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String?>()

    fun signInWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit) {
        loading.value = true
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        loading.value = false
                        if (task.isSuccessful) {
                            Log.d("NeoTrade_Loggin", "Inicio de sesión exitoso")
                            onSuccess()
                        } else {
                            Log.d("NeoTrade_Loggin", "Error: ${task.exception?.message}")
                            errorMessage.value = task.exception?.message
                        }
                    }
            } catch (e: Exception) {
                loading.value = false
                errorMessage.value = e.message
                Log.d("NeoTrade_Loggin", "Excepción: ${e.message}")
            }
        }
    }
}