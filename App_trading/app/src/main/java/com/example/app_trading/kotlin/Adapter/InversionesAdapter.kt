package com.example.app_trading.kotlin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.R
import com.example.app_trading.kotlin.CRUD.Entity.Inversion
import java.text.DecimalFormat

class InversionesAdapter(
    internal var inversiones: List<Inversion>,
    private val onItemClick: (Int) -> Unit // idAccion
) : RecyclerView.Adapter<InversionesAdapter.InversionViewHolder>() {

    class InversionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.textViewNombre)
        val cantidad: TextView = itemView.findViewById(R.id.textViewCantidad)
        val fecha: TextView = itemView.findViewById(R.id.textViewFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InversionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inversiones, parent, false) // Asegúrate que el nombre coincide con tu XML
        return InversionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InversionViewHolder, position: Int) {
        val inversion = inversiones[position]
        val decimalFormat = DecimalFormat("#,###.00")
        val formattedCantidad = decimalFormat.format(inversion.cantidad)
        holder.nombre.text = inversion.nombre
        holder.cantidad.text = "Cantidad: $formattedCantidad"
        holder.fecha.text = "Fecha: ${inversion.fecha}"
        holder.itemView.setOnClickListener {
            onItemClick(inversion.idInversiones) // Llama a la función con el idAccion
        }
    }
fun updateData(newList: List<Inversion>) {
    this.inversiones = newList
    notifyDataSetChanged()
}
    override fun getItemCount(): Int = inversiones.size
}