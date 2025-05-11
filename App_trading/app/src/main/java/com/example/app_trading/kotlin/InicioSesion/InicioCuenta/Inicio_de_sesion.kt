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
import com.example.app_trading.kotlin.CRUD.BaseDeDatos.DatabaseHelper
import com.example.app_trading.kotlin.CRUD.Entity.User
import com.example.app_trading.kotlin.CRUD.Entity.Accion
import com.example.app_trading.kotlin.CRUD.Entity.UsuarioActual
import com.example.app_trading.kotlin.InicioSesion.SplashScreenInicioDesesion
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
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                            fetchUserDataFromFirebase(userId) { user, acciones ->
                                if (user != null) {
                                    val dbHelper = DatabaseHelper(this)
                                    UsuarioActual.usuario = user // Asignar el usuario actual
                                    val nombreUsuario = UsuarioActual.usuario?.nombre ?: "Invitado"
                                    Toast.makeText(this, "Bienvenido, $nombreUsuario", Toast.LENGTH_SHORT).show()
                                    // Guardar usuario en la base de datos local
                                    dbHelper.insertUser(
                                        user.nombre ?: "", // Proporciona un valor predeterminado si es null
                                        user.apellido ?: "",
                                        user.apellido2 ?: "",
                                        user.gmail ?: "",
                                        user.password ?: "",
                                        user.fechaNaz ?: "",
                                        user.fechaUpdate ?: "",
                                        user.imageUrl ?: "",
                                        user.dinero ?: 0.0, // Proporciona un valor predeterminado para Double
                                        user.idAccion ?: 0 // Proporciona un valor predeterminado para Int
                                    )

                                    // Guardar acciones en la base de datos local
//                                    acciones.forEach { accion ->
//                                        dbHelper.insertAccion(
//                                            accion.nombre,
//                                            accion.ticker,
//                                            accion.sector,
//                                            accion.pais,
//                                            accion.divisa,
//                                            accion.fechaCreacion,
//                                            accion.fechaUpdate,
//                                            accion.precioAccion,
//                                            accion.precioCompra,
//                                            accion.cantidadAcciones,
//                                            userId.toInt()
//                                        )
//                                    }

                                    Log.d("Inicio_de_sesion", "Datos descargados y guardados localmente")
                                    val intent = Intent(this, SplashScreenInicioDesesion::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show()
                                }
                            }
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

        val dbHelper = DatabaseHelper(this)
        val users = dbHelper.getAllUsers()
        users.forEach { Log.d("User", it.toString()) }
    }

    private fun fetchUserDataFromFirebase(userId: String, onComplete: (User?, List<Accion>) -> Unit) {
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        // Obtener datos del usuario
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { userSnapshot ->
                val user = userSnapshot.toObject(User::class.java)

                // Obtener las acciones del usuario
                firestore.collection("acciones")
                    .whereEqualTo("idUser", userId)
                    .get()
                    .addOnSuccessListener { accionesSnapshot ->
                        val acciones = accionesSnapshot.toObjects(Accion::class.java)
                        onComplete(user, acciones)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        onComplete(null, emptyList())
                    }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                onComplete(null, emptyList())
            }
    }
}