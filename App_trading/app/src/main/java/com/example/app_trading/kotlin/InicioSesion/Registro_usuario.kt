package com.example.app_trading.kotlin.InicioSesion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_trading.MainActivity
import com.example.app_trading.R
import com.example.app_trading.kotlin.CRUD.BaseDeDatos.DatabaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Registro_usuario : AppCompatActivity() {
//    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var databaseHelper: DatabaseHelper
//    private lateinit var firebaseStorage: FirebaseStorage
//    private lateinit var firebaseStore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_usuario)

        // Obtener referencias a los elementos de la interfaz de usuario
        val eTxtCorreo = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val eTxtContrasena = findViewById<EditText>(R.id.editTextContrasena)
        val eTxtContrasenaConf = findViewById<EditText>(R.id.editTextConfContrasena)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el botón "Cancelar"
        findViewById<Button>(R.id.Cancelar).setOnClickListener {
            val intent = Intent(this, Inicio_de_sesion::class.java)
            startActivity(intent)
            finish() // Cierra la actividad actual
        }
    }

    private fun login_firebase(correo: String, contrasena: String) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//
                    var intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("correo", correo)//putExtra es para pasar datos entre actividades
                    intent.putExtra("Proveedor", "Usuario/Contraseña")
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Usuario/Contraseña incorrectos", Toast.LENGTH_SHORT).show()


//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        baseContext,
//                        "Authentication failed.",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                    updateUI(null)


                }
            }






    fun insertarUnUsuario() {
        // Aquí puedes implementar la lógica para insertar un nuevo usuario en la base de datos
        // utilizando la clase DatabaseHelper que creaste anteriormente.
        val dbHelper = DatabaseHelper(this)

// Insertar un usuario
        dbHelper.insertUser(
            nombre = "Juan",
            apellido = "Pérez",
            apellido2 = "González",
            gmail = "juanperez@gmail.com",
            password = "1234",
            imageUrl = "https://example.com/juan.jpg",
            fechaNaz = "1990-01-01",
            fechaUpdate = "2025-04-27 10:00:00",
            dinero = 1500.0,
            idAccion = 1
        )

// Obtener todos los usuarios
        val users = dbHelper.getAllUsers()
        users.forEach {
            println(it)
        }

// Actualizar un usuario
        dbHelper.updateUser(id = 1, nombre = "Juanito", gmail = "nuevoemail@gmail.com")

// Eliminar un usuario
        dbHelper.deleteUser(id = 1)

    }


    fun isFirebaseGmailRegistered(gmail: String, callback: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        auth.fetchSignInMethodsForEmail(gmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    callback(!signInMethods.isNullOrEmpty()) // Devuelve true si está registrado
                } else {
                    callback(false) // Error al verificar
                }
            }
    }
//    fun onBackPressed() {
//        // Evitar que el usuario vuelva a la pantalla de inicio de sesión
//        // al presionar el botón de retroceso
//        // super.onBackPressed() // Descomentar si deseas permitir el retroceso
//    }
}