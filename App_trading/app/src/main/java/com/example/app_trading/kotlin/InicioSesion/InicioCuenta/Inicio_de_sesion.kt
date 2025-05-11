package com.example.app_trading.kotlin.InicioSesion.InicioCuenta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.MainActivity
import com.example.app_trading.R
import com.example.app_trading.kotlin.InicioSesion.registroUsuario.FragmentoRegistro
import com.google.firebase.auth.FirebaseAuth

class Inicio_de_sesion : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_de_sesion)

        auth = FirebaseAuth.getInstance()

        val editTextCorreo = findViewById<EditText>(R.id.editTextEmail)
        val editTxtContrasena = findViewById<EditText>(R.id.editTextPassword)
        val botonLogueo = findViewById<Button>(R.id.loginButton)
        val btnCrearCuenta = findViewById<Button>(R.id.btnVCrearCuenta)

        botonLogueo.setOnClickListener {
            val email = editTextCorreo.text.toString()
            val password = editTxtContrasena.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Inicio_de_sesion", "Inicio de sesión exitoso")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.d("Inicio_de_sesion", "Error: ${task.exception?.message}")
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        btnCrearCuenta.setOnClickListener {
            val intent = Intent(this, FragmentoRegistro::class.java)
            startActivity(intent)
        }
    }
}