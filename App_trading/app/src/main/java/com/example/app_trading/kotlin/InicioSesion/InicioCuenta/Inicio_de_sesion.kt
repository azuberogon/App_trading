package com.example.app_trading.kotlin.InicioSesion.InicioCuenta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.app_trading.R
import com.example.app_trading.kotlin.CRUD.BaseDeDatos.DatabaseHelper
import com.example.app_trading.kotlin.CRUD.Entity.Accion
import com.example.app_trading.kotlin.CRUD.Entity.User
import com.example.app_trading.kotlin.CRUD.Entity.UsuarioActual
import com.example.app_trading.kotlin.InicioSesion.NotificationWorker
import com.example.app_trading.kotlin.InicioSesion.SplashScreenInicioDesesion
import com.example.app_trading.kotlin.InicioSesion.registroUsuario.FragmentoRegistro
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

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

        setupPeriodicNotifications()

        botonLogueo.setOnClickListener {
            val email = editTextCorreo.text.toString()
            val password = editTxtContrasena.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                            fetchUserDataFromFirebase(userId) { user, acciones ->
                                try {
                                    if (user != null) {
                                        val dbHelper = DatabaseHelper(this)
                                        dbHelper.borrarBaseDeDatos() // Borra todos los usuarios y acciones







                                        UsuarioActual.usuario = user

                                        val idUsuarioLocal = dbHelper.insertUser(
                                            user.nombre ?: "",
                                            user.apellido ?: "",
                                            user.apellido2 ?: "",
                                            user.gmail ?: "",
                                            user.password ?: "",
                                            user.imageUrl ?: "",
                                            user.fechaNaz ?: "",
                                            user.fechaUpdate ?: "",
                                            user.dinero ?: 0.0,
                                            user.idAccion
                                        ).toInt()

                                        acciones.forEach { accion ->
                                            dbHelper.insertAccion(
                                                accion.nombre ?: "",
                                                accion.ticker ?: "",
                                                accion.sector ?: "",
                                                accion.pais ?: "",
                                                accion.divisa ?: "",
                                                accion.fechaCreacion ?: "",
                                                accion.fechaUpdate ?: "",
                                                accion.precioAccion ?: 0.0,
                                                accion.precioCompra ?: 0.0,
                                                accion.cantidadAcciones ?: 0,
                                                idUsuarioLocal
                                            )
                                        }

                                        // SUBIR DATOS LOCALES A FIREBASE
                                        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                        val usuarioLocal = dbHelper.getAllUsers().find { it.gmail == user.gmail }

                                        if (usuarioLocal != null) {
                                            firestore.collection("users").document(userId)
                                                .set(usuarioLocal)
                                                .addOnSuccessListener {
                                                    val accionesUsuario = dbHelper.getAllInversiones().filter { it.idInversiones == usuarioLocal.id }
                                                    accionesUsuario.forEach { accion ->
                                                        val accionMap = hashMapOf(
                                                            "nombre" to accion.nombre,
                                                            "cantidadAcciones" to accion.cantidad,
                                                            "fechaCreacion" to accion.fecha,
                                                            "idUser" to userId
                                                        )
                                                        firestore.collection("acciones").add(accionMap)
                                                    }
                                                    Toast.makeText(this, "Datos subidos a Firebase", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(this, "Error al subir usuario", Toast.LENGTH_SHORT).show()
                                                }
                                        }

                                        Toast.makeText(this, "Sincronización exitosa", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, SplashScreenInicioDesesion::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("SyncError", "Error al sincronizar datos: ${e.message}")
                                    Toast.makeText(this, "Error al sincronizar datos", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this, FragmentoRegistro::class.java))
        }

        val dbHelper = DatabaseHelper(this)
        dbHelper.getAllUsers().forEach { Log.d("User", it.toString()) }
    }

    private fun fetchUserDataFromFirebase(userId: String, onComplete: (User?, List<Accion>) -> Unit) {
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { userSnapshot ->
                val user = userSnapshot.toObject(User::class.java)

                firestore.collection("acciones")
                    .whereEqualTo("idUser", userId)
                    .get()
                    .addOnSuccessListener { accionesSnapshot ->
                        val acciones = accionesSnapshot.toObjects(Accion::class.java)
                        onComplete(user, acciones)
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        onComplete(null, emptyList())
                    }
            }
            .addOnFailureListener {
                it.printStackTrace()
                onComplete(null, emptyList())
            }
    }

    private fun setupPeriodicNotifications() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .build()

        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            8, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "TradingAppNotificationWork",
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }

    
}
