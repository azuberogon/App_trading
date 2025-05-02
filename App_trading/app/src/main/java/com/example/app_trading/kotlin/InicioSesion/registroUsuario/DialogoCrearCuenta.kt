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
        fun show(fragmentManager: FragmentManager, nothing: Nothing?) {
            val dialog = DialogoCrearCuenta()
            dialog.show(fragmentManager, "DialogoCrearCuenta")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val temas = ContextThemeWrapper(activity, R.style.Theme_App_trading)
        val view: View = inflater.cloneInContext(temas).inflate(R.layout.fragment_dialogo_crear_cuenta, container, false)

        val editTextCorreo = view.findViewById<EditText>(R.id.editTxtCorreoElectronico)
        val eTxtNombreUsuario = view.findViewById<EditText>(R.id.editTxtNombreUsuario)
        val editTxtContrasena = view.findViewById<EditText>(R.id.editTxtcontrasena)
        val eTxtContrasenaConf = view.findViewById<EditText>(R.id.editTxtcontrasenaConf)
        //val btnCancelar = view.findViewById<Button>(R.id.btnCancelar)
//        val btnCrearCuenta = view.findViewById<Button>(R.id.btnCrearCuenta)

        val btnCrearCuenta = view.findViewById<Button>(R.id.btnCrearCuenta)


        btnCrearCuenta.setOnClickListener {
//            val intent = Intent(requireContext(), NotificacionUsuario::class.java)
//            startActivity(intent)
            if (editTxtContrasena.text.toString().isNotEmpty()) {
                if (editTextCorreo.text.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(editTextCorreo.text.toString()).matches()) {
                    editTextCorreo.error = null // Limpiar el error si el campo no está vacío
                    crearCuentaFirebase(editTextCorreo.text.toString(),editTxtContrasena.text.toString())
                } else {
                    editTextCorreo.error = "Campo vacío"
                    Toast.makeText(requireContext(), "Formato de correo incorrecto o vacio ", Toast.LENGTH_SHORT).show()
                    //return@setOnClickListener // Evita continuar si el campo está vacío

                }

            }else{
                editTxtContrasena.error = "Campo vacío"
                Toast.makeText(requireContext(), "Porfavor, rellene el campo contraseña", Toast.LENGTH_SHORT).show()

                //return@setOnClickListener // Evita continuar si el campo está vacío
            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    private fun crearCuentaFirebase(correo: String, contrasena: String) {


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithEmail:success")
//                    val user = firebaseAuth.currentUser
//                    updateUI(user)
                    var intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra("correo", task.result.user?.email)//putExtra es para pasar datos entre actividades
                    intent.putExtra("Proveedor", "Usuario/Contraseña")
                    startActivity(intent)
                    Toast.makeText( requireContext(), "Cuenta creada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText( requireContext(), "Contraseña corta/usuario existente", Toast.LENGTH_LONG).show()

                }
            }
    }
}