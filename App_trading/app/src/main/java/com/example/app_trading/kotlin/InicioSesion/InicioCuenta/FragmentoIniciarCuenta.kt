package com.example.app_trading.kotlin.InicioSesion.InicioCuenta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.app_trading.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentoIniciarCuenta : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_fragmento_iniciar_cuenta, container, false)
        var btnCrearCuenta = view.findViewById<Button>(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            Toast.makeText(requireContext(), "Mensaje enviado", Toast.LENGTH_LONG).show()
        }


        return view
    }


}