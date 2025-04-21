package com.example.app_trading.kotlin.InicioSesion.json.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.R
import com.example.app_trading.kotlin.InicioSesion.json.Model.busquedasEntity

class CustomAdapter(private val busquedaList: List<busquedasEntity>):
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

//    var nombreAccion = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") // aqui se van a cargar los datos hay que mirar como hacae para que los datos se pase del otro rv
//    var valorAccion = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
//    //aqui falta la validacion para subir o bajar que se pondra con los datos para saber si subio o bajo respecto al anterior
//    val images = intArrayOf(R.drawable.baseline_arrow_drop_up_24,
//        R.drawable.baseline_arrow_drop_down_24,
//        R.drawable.baseline_arrow_drop_up_24,
//        R.drawable.baseline_arrow_drop_down_24,
//        R.drawable.baseline_arrow_drop_up_24)


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemNombreAccion: TextView
        var itemValorAccion: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemNombreAccion = itemView.findViewById(R.id.item_nombreAccion)
            itemValorAccion = itemView.findViewById(R.id.item_valorAccion)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(viewholder: ViewHolder, i: Int) {
        val stock = busquedaList[i]
        viewholder.itemNombreAccion.text = stock.name
        viewholder.itemValorAccion.text = stock.name


//        viewholder.itemNombreAccion.text = nombreAccion[i]
//        viewholder.itemValorAccion.text = valorAccion[i]
//        viewholder.itemImage.setImageResource(images[i])
    }

    override fun getItemCount(): Int {
        return busquedaList.size
    }




}