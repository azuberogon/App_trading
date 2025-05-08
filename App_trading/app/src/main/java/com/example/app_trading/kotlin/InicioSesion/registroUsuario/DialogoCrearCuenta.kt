package com.example.app_trading.kotlin.InicioSesion.registroUsuario

            import android.content.Intent
            import android.graphics.Color
            import android.graphics.drawable.ColorDrawable
            import android.os.Bundle
            import android.util.Patterns
            import android.view.ContextThemeWrapper
            import android.view.LayoutInflater
            import android.view.View
            import android.view.ViewGroup
            import android.widget.Button
            import android.widget.EditText
            import android.widget.Toast
            import androidx.fragment.app.DialogFragment
            import androidx.fragment.app.FragmentManager
            import com.example.app_trading.MainActivity
            import com.example.app_trading.R
            import com.google.firebase.auth.FirebaseAuth

            class DialogoCrearCuenta : DialogFragment() {

                companion object {
                    fun show(fragmentManager: FragmentManager) {
                        DialogoCrearCuenta().show(fragmentManager, "DialogoCrearCuenta")
                    }
                }

                override fun onCreateView(
                    inflater: LayoutInflater, container: ViewGroup?,
                    savedInstanceState: Bundle?
                ): View? {
                    val contextThemeWrapper = ContextThemeWrapper(activity, R.style.Theme_App_trading)
                    val view = inflater.cloneInContext(contextThemeWrapper)
                        .inflate(R.layout.fragment_dialogo_crear_cuenta, container, false)

                    val editTextCorreo = view.findViewById<EditText>(R.id.editTxtCorreoElectronico)
                    val editTxtContrasena = view.findViewById<EditText>(R.id.editTxtcontrasena)
                    val btnCrearCuenta = view.findViewById<Button>(R.id.btnCrearCuenta)

                    btnCrearCuenta.setOnClickListener {
                        val correo = editTextCorreo.text.toString()
                        val contrasena = editTxtContrasena.text.toString()

                        when {
                            contrasena.isEmpty() -> {
                                editTxtContrasena.error = "Campo vacío"
                                showToast("Por favor, rellene el campo contraseña")
                            }
                            correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                                editTextCorreo.error = "Correo inválido"
                                showToast("Formato de correo incorrecto o vacío")
                            }
                            else -> crearCuentaFirebase(correo, contrasena)
                        }
                    }

                    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    return view
                }

                private fun crearCuentaFirebase(correo: String, contrasena: String) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                                    putExtra("correo", task.result.user?.email)
                                    putExtra("Proveedor", "Usuario/Contraseña")
                                }
                                startActivity(intent)
                                showToast("Cuenta creada")
                            } else {
                                showToast("Contraseña corta o usuario existente")
                            }
                        }
                }

                private fun showToast(message: String) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }