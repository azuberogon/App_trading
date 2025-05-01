package com.example.app_trading.kotlin.InicioSesion


import android.content.ContentValues.TAG
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
import com.google.firebase.auth.FirebaseAuth

class Inicio_de_sesion : AppCompatActivity() {




    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_de_sesion) // Carga el layout que creamos

        // Obtener referencias a los elementos de la interfaz de usuario
        val correoEditText = findViewById<EditText>(R.id.editTextCorreoElectronico)
        val contrasenaEditText = findViewById<EditText>(R.id.editTextContraseñaCorreo)
        val botonLogueo = findViewById<Button>(R.id.btnInicioDeSesion)
        val botonRegistro = findViewById<Button>(R.id.btnRegistro)

        // Configurar el listener para el botón de Iniciar Sesión
        botonLogueo.setOnClickListener {
            if (contrasenaEditText.text.toString().isNotEmpty()) {
                if (correoEditText.text.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(correoEditText.text.toString()).matches()) {
                    correoEditText.error = null // Limpiar el error si el campo no está vacío
                    login_firebase(correoEditText.text.toString(),contrasenaEditText.text.toString())
                } else {
                    correoEditText.error = "Campo vacío"
                    Toast.makeText(this, "Formato de correo incorrecto o vacio ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Evita continuar si el campo está vacío

                }

            }else{
                contrasenaEditText.error = "Campo vacío"
                Toast.makeText(this, "Porfavor, rellene el campo contraseña", Toast.LENGTH_SHORT).show()

                return@setOnClickListener // Evita continuar si el campo está vacío
            }
            val username = correoEditText.text.toString()
            val password = contrasenaEditText.text.toString()

            // Aquí debes realizar la lógica de autenticación.
            // Por ejemplo, verificar contra una base de datos o un servicio web.
            if (true) { // isValidCredentials(username, password)
                // Si las credenciales son válidas, inicia la nueva Activity.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cierra la Inicio_de_sesion para que el usuario no pueda volver atrás.
            } else {
                // Si las credenciales son inválidas, muestra un mensaje de error.
                Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el listener para el botón de Registrarse
        botonRegistro.setOnClickListener {
            // Aquí debes iniciar la Activity de registro.
//            val intent = Intent(this, Registro_usuario::class.java)
//            startActivity(intent)
            //Registro_usuario().show(supportFragmentManager, null)
        }
    }

    private fun login_firebase(correo: String, contrasena: String) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithEmail:success")
//                    val user = firebaseAuth.currentUser
//                    updateUI(user)
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




//        firebaseAuth.signInWithEmailAndPassword(correo, contrasena)
//             .addOnCompleteListener { task ->
//                 if (task.isSuccessful) {
//                     // Inicio de sesión exitoso
//                     val intent = Intent(this, MainActivity::class.java)
//                     startActivity(intent)
//                     finish()
//                 } else {
//                     // Error en el inicio de sesión
//                     Toast.makeText(this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show()
//                 }
//             }

//        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = firebaseAuth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        baseContext,
//                        "Authentication failed.",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                    updateUI(null)
//                }
//            }




    }

    // Esta función es un ejemplo. Debes implementar tu propia lógica de validación.
    private fun isValidCredentials(username: String, password: String): Boolean {
        // Reemplaza esto con tu lógica de autenticación.
        return username == "usuario" && password == "contraseña"
    }

}