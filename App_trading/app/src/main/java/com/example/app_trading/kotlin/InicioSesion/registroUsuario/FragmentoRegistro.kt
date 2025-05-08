package com.example.app_trading.kotlin.InicioSesion.registroUsuario

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.app_trading.MainActivity
import com.example.app_trading.R
import com.google.firebase.auth.FirebaseAuth

class FragmentoRegistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fragmento_registro)

        var btnAbrirDialog= findViewById<Button>(R.id.btnAbrirDialog)

        btnAbrirDialog.setOnClickListener {
            val dialogo = DialogoCrearCuenta()
            dialogo.show(supportFragmentManager, null)
        }

//        btnAbrirDialog.setOnClickListener {
//            val dialogo = DialogoContacto()
//            dialogo.show(supportFragmentManager, "DialogFragment")
//        }

    }


    private fun crearCuentaFirebase(correo: String, contrasena: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("correo", task.result.user?.email)
                    intent.putExtra("Proveedor", "Usuario/Contraseña")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}