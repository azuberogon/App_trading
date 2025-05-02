package com.example.app_trading.kotlin.InicioSesion.registroUsuario

import androidx.appcompat.app.AppCompatActivity

class Registro_usuario : AppCompatActivity()  {

    //    private lateinit var imageView: ImageView
//    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var databaseHelper: DatabaseHelper
//    private lateinit var firebaseStorage: FirebaseStorage
//    private lateinit var firebaseStore: FirebaseFirestore


/*
    override fun onCreateView(
        menuInflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        var view = menuInflater.inflate(R.layout.activity_registro_usuario, container, false)
        val eTxtCorreo = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val eTxtContrasena = view.findViewById<EditText>(R.id.editTextContrasena)
        val eTxtContrasenaConf = view.findViewById<EditText>(R.id.editTextConfContrasena)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelar)
        val btnCrearCuenta = view.findViewById<Button>(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            if (eTxtContrasena.text.toString().isNotEmpty()) {
                if (eTxtCorreo.text.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                        eTxtCorreo.text.toString()
                    ).matches()
                ) {
                    eTxtCorreo.error = null // Limpiar el error si el campo no está vacío
                    crearCuentaFirebase(eTxtCorreo.text.toString(), eTxtContrasena.text.toString())
                } else {
                    eTxtCorreo.error = "Campo vacío"
                    Toast.makeText(
                        this,
                        "Formato de correo incorrecto o vacio ",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener // Evita continuar si el campo está vacío

                }

            } else {
                eTxtContrasena.error = "Campo vacío"
                Toast.makeText(this, "Porfavor, rellene el campo contraseña", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener // Evita continuar si el campo está vacío
            }
        }
        return view
    }

    private fun crearCuentaFirebase(correo: String, contrasena: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
//
                    var intent = Intent(requireContext(), Registro_usuario::class.java)
                    intent.putExtra("correo", correo)//putExtra es para pasar datos entre actividades
                    intent.putExtra("Proveedor", "Usuario/Contraseña")
                    startActivity(intent)

                } else {
                    Toast.makeText(requireContext(), "Usuario/Contraseña incorrectos", Toast.LENGTH_SHORT).show()

                }


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
        btnCancelar.setOnClickListener {
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
                    intent.putExtra(
                        "correo",
                        correo
                    )//putExtra es para pasar datos entre actividades
                    intent.putExtra("Proveedor", "Usuario/Contraseña")
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Usuario/Contraseña incorrectos", Toast.LENGTH_SHORT)
                        .show()


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
*/
}