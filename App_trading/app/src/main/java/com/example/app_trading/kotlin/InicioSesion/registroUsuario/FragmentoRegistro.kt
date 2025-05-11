package com.example.app_trading.kotlin.InicioSesion.registroUsuario

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentoRegistro : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragmento_registro)

        auth = FirebaseAuth.getInstance()

        val editTxtNombre = findViewById<TextInputEditText>(R.id.editTxtNombre)
        val editTxtNombreUsuario = findViewById<TextInputEditText>(R.id.editTxtNombreUsuario)
        val editTxtCorreo = findViewById<TextInputEditText>(R.id.editTxtCorreoElectronico)
        val editTxtContrasena = findViewById<TextInputEditText>(R.id.editTxtcontrasena)
        val editTxtContrasenaConf = findViewById<TextInputEditText>(R.id.editTxtcontrasenaConf)
        val btnCrearCuenta = findViewById<Button>(R.id.btnFCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            val nombre = editTxtNombre.text.toString()
            val nombreUsuario = editTxtNombreUsuario.text.toString()
            val correo = editTxtCorreo.text.toString()
            val contrasena = editTxtContrasena.text.toString()
            val contrasenaConf = editTxtContrasenaConf.text.toString()

            if (validarCampos(nombre, nombreUsuario, correo, contrasena, contrasenaConf)) {
                registrarUsuarioEnFirebase(nombre, nombreUsuario, correo, contrasena)
            }
        }
    }

    private fun validarCampos(
        nombre: String,
        nombreUsuario: String,
        correo: String,
        contrasena: String,
        contrasenaConf: String
    ): Boolean {
        return when {
            nombre.isEmpty() -> {
                mostrarError("El campo nombre está vacío")
                false
            }
            nombreUsuario.isEmpty() -> {
                mostrarError("El campo nombre de usuario está vacío")
                false
            }
            correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                mostrarError("El correo es inválido o está vacío")
                false
            }
            contrasena.isEmpty() -> {
                mostrarError("El campo contraseña está vacío")
                false
            }
            contrasena != contrasenaConf -> {
                mostrarError("Las contraseñas no coinciden")
                false
            }
            else -> true
        }
    }

    private fun registrarUsuarioEnFirebase(
        nombre: String,
        nombreUsuario: String,
        correo: String,
        contrasena: String
    ) {
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result.user?.uid ?: return@addOnCompleteListener
                    val userData = mapOf(
                        "nombre" to nombre,
                        "nombreUsuario" to nombreUsuario,
                        "correo" to correo
                    )
                    firestore.collection("users").document(userId).set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            mostrarError("Error al guardar datos: ${e.message}")
                        }
                } else {
                    mostrarError("Error: ${task.exception?.message}")
                }
            }
    }

    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}