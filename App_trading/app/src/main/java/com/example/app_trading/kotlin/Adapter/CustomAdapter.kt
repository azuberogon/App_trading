package com.example.app_trading.kotlin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.R
import com.example.app_trading.kotlin.Model.busquedasEntity

    class CustomAdapter(
        private val busquedaList: List<busquedasEntity>,
        private val onItemClick: (busquedasEntity) -> Unit
    ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val itemImage: ImageView = itemView.findViewById(R.id.item_image)
            val itemNombreAccion: TextView = itemView.findViewById(R.id.item_nombreAccion)
            val itemValorAccion: TextView = itemView.findViewById(R.id.item_valorAccion)

            init {
                itemView.setOnClickListener {
                    onItemClick(busquedaList[adapterPosition])
                }
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(viewholder: ViewHolder, i: Int) {
            val nameBusqueda = busquedaList[i]
            viewholder.itemNombreAccion.text = nameBusqueda.name
            viewholder.itemValorAccion.text = "Ticker: ${nameBusqueda.ticker}"

            if (nameBusqueda.isActive) {
                viewholder.itemImage.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            } else {
                viewholder.itemImage.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }
        }

        override fun getItemCount(): Int = busquedaList.size
    }



//}