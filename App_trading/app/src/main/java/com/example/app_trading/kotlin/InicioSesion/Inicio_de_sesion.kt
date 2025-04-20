package com.example.app_trading.kotlin.InicioSesion


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.MainActivity
import com.example.app_trading.R

class Inicio_de_sesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_de_sesion) // Carga el layout que creamos

        // Obtener referencias a los elementos de la interfaz de usuario
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        //val aboutTextView = findViewById<TextView>(R.id.aboutTextView)

        // Configurar el listener para el botón de Iniciar Sesión
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Aquí debes realizar la lógica de autenticación.
            // Por ejemplo, verificar contra una base de datos o un servicio web.
            if (true) {//isValidCredentials(username, password)
                // Si las credenciales son válidas, inicia la nueva Activity.
                // Si las credenciales son válidas, inicia la MainActivity.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cierra la Inicio_de_sesion para que el usuario no pueda volver atrás.
                //finish() // Opcional: cierra la LoginActivity para que el usuario no pueda volver atrás.

            } else {
                // Si las credenciales son inválidas, muestra un mensaje de error.
                Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el listener para el botón de Registrarse
        registerButton.setOnClickListener {
            // Aquí debes iniciar la Activity de registro.
            val intent = Intent(this, Registro_usuario::class.java)
            startActivity(intent)
        }

        // Configurar el listener para el TextView "About"
//        aboutTextView.setOnClickListener {
//            // Aquí debes iniciar la Activity "About" o mostrar un diálogo con información.
//            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
//            // Puedes reemplazar el Toast por un Intent a una Activity "About"
//        }
    }

    // Esta función es un ejemplo. Debes implementar tu propia lógica de validación.
    private fun isValidCredentials(username: String, password: String): Boolean {
        // Reemplaza esto con tu lógica de autenticación.
        return username == "usuario" && password == "contraseña"
    }
}