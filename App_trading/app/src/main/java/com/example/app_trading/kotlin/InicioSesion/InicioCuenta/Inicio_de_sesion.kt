package com.example.app_trading.kotlin.InicioSesion.InicioCuenta


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.MainActivity
import com.example.app_trading.R
import com.example.app_trading.kotlin.InicioSesion.registroUsuario.DialogoCrearCuenta
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Inicio_de_sesion : AppCompatActivity() {
        private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_inicio_de_sesion)

            // Inicializar FirebaseAuth
            FirebaseApp.initializeApp(this)
            auth = FirebaseAuth.getInstance()

            // Referencias a los elementos de la interfaz de usuario
            val editTextCorreo = findViewById<EditText>(R.id.editTextCorreoElectronico)
            val editTxtContrasena = findViewById<EditText>(R.id.editTextContraseñaCorreo)
            val botonLogueo = findViewById<Button>(R.id.btnCrearCuenta)
            val botonRegistro = findViewById<Button>(R.id.btnRegistro1)

            // Listener para el botón de iniciar sesión
            botonLogueo.setOnClickListener {
                val email = editTextCorreo.text.toString()
                val password = editTxtContrasena.text.toString()

                if (password.isNotEmpty()) {
                    if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        editTextCorreo.error = null
                        login_firebase(email, password)
                    } else {
                        editTextCorreo.error = "Formato de correo incorrecto o vacío"
                        Toast.makeText(this, "Formato de correo incorrecto o vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    editTxtContrasena.error = "Campo vacío"
                    Toast.makeText(this, "Por favor, rellene el campo contraseña", Toast.LENGTH_SHORT).show()
                }
            }

            // Listener para el botón de registro
            botonRegistro.setOnClickListener {
                DialogoCrearCuenta.show(supportFragmentManager, null)
            }
        }


    private fun login_firebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Inicio_de_sesion", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w("Inicio_de_sesion", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Error: Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun updateUI2(user: FirebaseUser?) {
        if (user != null) {
            // Si el inicio de sesión es exitoso, redirige al usuario a la actividad principal
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("correo", user.email) // Pasar el correo del usuario
            startActivity(intent)
            finish() // Finaliza la actividad actual
        } else {
            // Si el inicio de sesión falla, muestra un mensaje
            Toast.makeText(this, "Por favor, verifica tus credenciales.", Toast.LENGTH_SHORT).show()
        }
    }

    // Esta función es un ejemplo. Debes implementar tu propia lógica de validación.
    private fun isValidCredentials(username: String, password: String): Boolean {
        // Reemplaza esto con tu lógica de autenticación.
        return username == "usuario" && password == "contraseña"
    }








    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }
    // [END on_start_check_user]

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("correo", user.email)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Por favor, verifica tus credenciales.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun reload() {
    }

    companion object {
        private const val TAG = "EmailPassword"
    }























}




