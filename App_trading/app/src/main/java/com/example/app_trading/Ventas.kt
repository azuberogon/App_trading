
package com.example.app_trading

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_trading.kotlin.CRUD.BaseDeDatos.DatabaseHelper
import com.example.app_trading.kotlin.CRUD.Entity.UsuarioActual

class Ventas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_sell)

        val dbHelper = DatabaseHelper(this)
        val inputSell = findViewById<EditText>(R.id.inputSell)
        val btnConfirmSell = findViewById<Button>(R.id.btnConfirmSell)

        val idAccion = intent.getIntExtra("idAccion", -1)
        val accion = dbHelper.getAccionById(idAccion)
        val usuario = UsuarioActual.usuario

        if (usuario == null || usuario.id == null) {
            Toast.makeText(this, "Usuario no asignado o ID inválido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Usuario asignado: ${usuario.nombre}, ID: ${usuario.id}", Toast.LENGTH_SHORT).show()
        }

        val TAG = "UsuarioDebug"

        if (usuario == null || usuario.id == null) {
            Log.d(TAG, "Usuario no asignado o ID inválido")
        } else {
            Log.d(TAG, "Usuario asignado: ${usuario.nombre}, ID: ${usuario.id}")
        }

        btnConfirmSell.setOnClickListener {
            val cantidadAVender = inputSell.text.toString().toIntOrNull() ?: 0
            if (cantidadAVender > 0 && accion != null && usuario != null) {
                val cantidadAcciones = accion.cantidadAcciones ?: 0
                val precioAccion = accion.precioAccion ?: 0.0
                val idAccionLocal = accion.id ?: -1
                val idUsuarioLocal = usuario.id ?: -1

                Toast.makeText(this, "Tienes $cantidadAcciones acciones", Toast.LENGTH_SHORT).show()
                if (cantidadAcciones >= cantidadAVender) {
                    val nuevoTotalAcciones = cantidadAcciones - cantidadAVender
                    val dineroGanado = cantidadAVender * precioAccion
                    val nuevoDineroUsuario = (usuario.dinero ?: 0.0) + dineroGanado

                    // Aquí va la lógica de eliminar o actualizar
                    if (nuevoTotalAcciones == 0) {
                        dbHelper.eliminarAccion(idAccionLocal)
                    } else {
                        dbHelper.actualizarCantidadAcciones(idAccionLocal, nuevoTotalAcciones)
                    }
                    dbHelper.actualizarDineroUsuario(idUsuarioLocal, nuevoDineroUsuario)

                    Toast.makeText(this, "Venta realizada", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "No tienes suficientes acciones", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}