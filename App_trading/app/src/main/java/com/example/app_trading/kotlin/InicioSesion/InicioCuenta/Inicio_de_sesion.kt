package com.example.app_trading.kotlin.InicioSesion.InicioCuenta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
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

/**
 * Actividad responsable del inicio de sesión de usuarios.
 * Permite a los usuarios autenticarse utilizando correo electrónico y contraseña
 * a través de Firebase Authentication. Tras un inicio de sesión exitoso,
 * descarga los datos del usuario y sus acciones asociadas desde Firestore
 * y los guarda en la base de datos SQLite local.
 */
class Inicio_de_sesion : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    /**
     * Método onCreate de la actividad. Se llama cuando se crea la actividad.
     * Configura la interfaz de usuario, inicializa Firebase Authentication
     * y los listeners de los botones de inicio de sesión y creación de cuenta.
     * También recupera y loguea todos los usuarios de la base de datos local al inicio.
     *
     * @param savedInstanceState Estado de instancia previamente guardado, si lo hay.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_de_sesion)

        auth = FirebaseAuth.getInstance()

        val editTextCorreo = findViewById<EditText>(R.id.editTextEmail)
        val editTxtContrasena = findViewById<EditText>(R.id.editTextPassword)
        val botonLogueo = findViewById<Button>(R.id.loginButton)
        val btnCrearCuenta = findViewById<Button>(R.id.btnVCrearCuenta)
        setupPeriodicNotifications()
        // Configurar el listener para el botón de inicio de sesión
        botonLogueo.setOnClickListener {
            val email = editTextCorreo.text.toString()
            val password = editTxtContrasena.text.toString()

            // Validar que los campos de correo y contraseña no estén vacíos
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Intentar iniciar sesión con Firebase Authentication
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Inicio de sesión exitoso, obtener el ID del usuario
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                            // Descargar datos del usuario y sus acciones desde Firebase
                            fetchUserDataFromFirebase(userId) { user, acciones ->
                                if (user != null) {
                                    // Si se obtuvieron los datos del usuario
                                    val dbHelper = DatabaseHelper(this)
                                    UsuarioActual.usuario = user // Asignar el usuario actual a un objeto Singleton
                                    val nombreUsuario = UsuarioActual.usuario?.nombre ?: "Invitado"
                                    Toast.makeText(this, "Bienvenido, $nombreUsuario", Toast.LENGTH_SHORT).show()

                                    // Guardar usuario en la base de datos local SQLite
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

                                    // Guardar acciones en la base de datos local (actualmente comentado)
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

                                    // Iniciar la siguiente actividad (SplashScreenInicioDesesion)
                                    val intent = Intent(this, SplashScreenInicioDesesion::class.java)
                                    startActivity(intent)
                                    finish() // Finalizar esta actividad para que el usuario no pueda volver atrás
                                } else {
                                    // Error al obtener datos del usuario
                                    Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // Inicio de sesión fallido, mostrar mensaje de error
                            Log.d("Inicio_de_sesion", "Error: ${task.exception?.message}")
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Campos vacíos, mostrar mensaje al usuario
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el listener para el botón de crear cuenta
        btnCrearCuenta.setOnClickListener {
            // Iniciar la actividad para crear una nueva cuenta
            val intent = Intent(this, FragmentoRegistro::class.java)
            startActivity(intent)
        }

        // Recuperar y loguear todos los usuarios de la base de datos local al inicio
        val dbHelper = DatabaseHelper(this)
        val users = dbHelper.getAllUsers()
        users.forEach { Log.d("User", it.toString()) }
    }

    /**
     * Descarga los datos de un usuario específico y sus acciones asociadas desde Firestore.
     * Esta función es asíncrona y llama a un callback al completar la operación.
     *
     * @param userId El ID del usuario cuyos datos se desean obtener.
     * @param onComplete Una función lambda que se llama cuando la operación se completa.
     *                   Recibe un objeto [User] (puede ser null si no se encuentra el usuario)
     *                   y una [List] de [Accion] (puede estar vacía si no hay acciones o hay un error).
     */
    private fun fetchUserDataFromFirebase(userId: String, onComplete: (User?, List<Accion>) -> Unit) {
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        // Obtener datos del usuario desde la colección "users" en Firestore
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { userSnapshot ->
                // Mapear el documento de usuario a un objeto User
                val user = userSnapshot.toObject(User::class.java)

                // Obtener las acciones asociadas a este usuario desde la colección "acciones"
                firestore.collection("acciones")
                    .whereEqualTo("idUser", userId) // Filtra acciones por el ID del usuario
                    .get()
                    .addOnSuccessListener { accionesSnapshot ->
                        // Mapear los documentos de acciones a objetos Accion
                        val acciones = accionesSnapshot.toObjects(Accion::class.java)
                        // Llamar al callback onComplete con los datos del usuario y sus acciones
                        onComplete(user, acciones)
                    }
                    .addOnFailureListener { e ->
                        // Manejar errores al obtener las acciones
                        e.printStackTrace()
                        onComplete(null, emptyList()) // Llamar al callback con null para el usuario y lista vacía para acciones
                    }
            }
            .addOnFailureListener { e ->
                // Manejar errores al obtener los datos del usuario
                e.printStackTrace()
                onComplete(null, emptyList()) // Llamar al callback con null para el usuario y lista vacía para acciones
            }
    }
    /**
     * Configura un trabajo periódico con WorkManager para enviar notificaciones.
     */
    private fun setupPeriodicNotifications() {
        // Definir restricciones (opcional)
        val constraints = Constraints.Builder()
            // La importación correcta para NetworkType es androidx.work.NetworkType
            .setRequiredNetworkType(androidx.work.NetworkType.NOT_REQUIRED) // No requiere red
            .setRequiresBatteryNotLow(false) // No requiere que la batería no esté baja
            .build()

        // Crear una solicitud de trabajo periódico
        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            8, TimeUnit.HOURS // Intervalo de repetición: 8 horas
        )
            .setConstraints(constraints) // Aplicar restricciones
            // .setInitialDelay(10, TimeUnit.SECONDS) // Opcional: retraso inicial
            .build()

        // Encolar el trabajo con WorkManager
        // Usa ExistingPeriodicWorkPolicy.KEEP para no reemplazar una tarea existente con el mismo nombre
        // si ya está en ejecución o en cola. Usa REPLACE si quieres asegurarte de que solo haya una
        // tarea de este tipo en cualquier momento.
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "TradingAppNotificationWork", // Nombre único para tu trabajo
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }
}