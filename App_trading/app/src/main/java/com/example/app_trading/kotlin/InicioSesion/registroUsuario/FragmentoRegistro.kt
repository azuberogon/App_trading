package com.example.app_trading.kotlin.InicioSesion.registroUsuario

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.R
import com.example.app_trading.kotlin.InicioSesion.SplashScreenInicioDesesion
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Actividad responsable del registro de nuevos usuarios.
 * Permite a los usuarios crear una cuenta utilizando correo electrónico y contraseña
 * a través de Firebase Authentication, y guarda información adicional del usuario en Firestore.
 */
class FragmentoRegistro : AppCompatActivity() {

    // Instancia de Firebase Authentication para la autenticación de usuarios.
    private lateinit var auth: FirebaseAuth
    // Instancia de Firebase Firestore para almacenar datos adicionales del usuario.
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Método onCreate de la actividad. Se llama cuando se crea la actividad.
     * Configura la interfaz de usuario, inicializa Firebase Authentication
     * y configura el listener para el botón de creación de cuenta.
     *
     * @param savedInstanceState Estado de instancia previamente guardado, si lo hay.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragmento_registro)

        // Inicializar la instancia de Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Obtener referencias a los elementos de la UI
        val editTxtNombre = findViewById<TextInputEditText>(R.id.editTxtNombre)
        val editTxtNombreUsuario = findViewById<TextInputEditText>(R.id.editTxtNombreUsuario)
        val editTxtCorreo = findViewById<TextInputEditText>(R.id.editTxtCorreoElectronico)
        val editTxtContrasena = findViewById<TextInputEditText>(R.id.editTxtcontrasena)
        val editTxtContrasenaConf = findViewById<TextInputEditText>(R.id.editTxtcontrasenaConf)
        val btnCrearCuenta = findViewById<Button>(R.id.btnFCrearCuenta)

        // Configurar el listener para el botón de crear cuenta
        btnCrearCuenta.setOnClickListener {
            // Obtener el texto de los campos de entrada
            val nombre = editTxtNombre.text.toString()
            val nombreUsuario = editTxtNombreUsuario.text.toString()
            val correo = editTxtCorreo.text.toString()
            val contrasena = editTxtContrasena.text.toString()
            val contrasenaConf = editTxtContrasenaConf.text.toString()

            // Validar los campos antes de intentar registrar al usuario
            if (validarCampos(nombre, nombreUsuario, correo, contrasena, contrasenaConf)) {
                // Si los campos son válidos, proceder con el registro en Firebase
                registrarUsuarioEnFirebase(nombre, nombreUsuario, correo, contrasena)
            }
        }
    }

    /**
     * Valida los campos de entrada del formulario de registro.
     * Comprueba si los campos obligatorios están vacíos, si el correo tiene un formato válido
     * y si las contraseñas coinciden. Muestra mensajes de error si alguna validación falla.
     *
     * @param nombre El nombre introducido por el usuario.
     * @param nombreUsuario El nombre de usuario introducido por el usuario.
     * @param correo El correo electrónico introducido por el usuario.
     * @param contrasena La contraseña introducida por el usuario.
     * @param contrasenaConf La confirmación de la contraseña introducida por el usuario.
     * @return `true` si todos los campos son válidos, `false` en caso contrario.
     */
    private fun validarCampos(
        nombre: String,
        nombreUsuario: String,
        correo: String,
        contrasena: String,
        contrasenaConf: String
    ): Boolean {
        return when {
            // Comprobar si el campo nombre está vacío
            nombre.isEmpty() -> {
                mostrarError("El campo nombre está vacío")
                false
            }
            // Comprobar si el campo nombre de usuario está vacío
            nombreUsuario.isEmpty() -> {
                mostrarError("El campo nombre de usuario está vacío")
                false
            }
            // Comprobar si el campo correo está vacío o si el formato no es válido
            correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                mostrarError("El correo es inválido o está vacío")
                false
            }
            // Comprobar si el campo contraseña está vacío
            contrasena.isEmpty() -> {
                mostrarError("El campo contraseña está vacío")
                false
            }
            // Comprobar si las contraseñas coinciden
            contrasena != contrasenaConf -> {
                mostrarError("Las contraseñas no coinciden")
                false
            }
            // Si todas las validaciones pasan, los campos son válidos
            else -> true
        }
    }

    /**
     * Registra un nuevo usuario en Firebase Authentication con el correo y contraseña proporcionados,
     * y luego guarda información adicional del usuario (nombre, nombre de usuario, correo) en Firestore.
     * Muestra mensajes de éxito o error al completar la operación.
     *
     * @param nombre El nombre del usuario a registrar.
     * @param nombreUsuario El nombre de usuario a registrar.
     * @param correo El correo electrónico del usuario a registrar (se usa para la autenticación).
     * @param contrasena La contraseña del usuario a registrar (se usa para la autenticación).
     */
    private fun registrarUsuarioEnFirebase(
        nombre: String,
        nombreUsuario: String,
        correo: String,
        contrasena: String
    ) {
        // Crear usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                // Verificar si la creación del usuario fue exitosa
                if (task.isSuccessful) {
                    // Obtener el ID único del usuario recién creado
                    val userId = task.result.user?.uid ?: return@addOnCompleteListener
                    // Crear un mapa con los datos adicionales del usuario para guardar en Firestore
                    val userData = mapOf(
                        "nombre" to nombre,
                        "nombreUsuario" to nombreUsuario,
                        "correo" to correo
                    )
                    // Guardar los datos del usuario en la colección "users" de Firestore, usando el userId como clave del documento
                    firestore.collection("users").document(userId).set(userData)
                        .addOnSuccessListener {
                            // Si los datos se guardaron con éxito en Firestore
                            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                            // Iniciar la siguiente actividad (SplashScreenInicioDesesion)
                            val intent = Intent(this, SplashScreenInicioDesesion::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            // Si ocurrió un error al guardar los datos en Firestore
                            mostrarError("Error al guardar datos: ${e.message}")
                        }
                } else {
                    // Si ocurrió un error al crear el usuario en Firebase Authentication
                    mostrarError("Error: ${task.exception?.message}")
                }
            }
    }

    /**
     * Muestra un mensaje corto (Toast) al usuario.
     *
     * @param mensaje El mensaje a mostrar.
     */
    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}