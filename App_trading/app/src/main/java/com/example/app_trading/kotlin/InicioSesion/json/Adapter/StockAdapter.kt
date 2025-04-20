package com.example.app_trading.kotlin.InicioSesion.json.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.R
import com.example.app_trading.kotlin.InicioSesion.json.Model.StockPrice

/**
 * `StockAdapter` es un adaptador para un `RecyclerView` que muestra una lista de precios de acciones.
 *
 * Este adaptador toma una lista de objetos `StockPrice` y los muestra en una lista desplazable.
 * Cada elemento de la lista muestra la fecha y el precio de cierre de una acción.
 *
 * @property stockList La lista de objetos `StockPrice` que se mostrarán en el `RecyclerView`.
 */
class StockAdapter(private val stockList: List<StockPrice>) :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    /**
     * `StockViewHolder` es una clase interna que representa una vista individual (un elemento) en el `RecyclerView`.
     *
     * Mantiene referencias a los `TextView` que muestran la fecha y el precio de cierre de la acción.
     *
     * @property itemView La vista raíz del elemento.
     * @property date El `TextView` que muestra la fecha.
     * @property closePrice El `TextView` que muestra el precio de cierre.
     */
    class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val closePrice: TextView = itemView.findViewById(R.id.closePrice)
    }

    /**
     * `onCreateViewHolder` se llama cuando el `RecyclerView` necesita crear una nueva vista para un elemento.
     *
     * Infla el layout `row_stock` para crear la vista del elemento y crea un nuevo `StockViewHolder` para mantenerla.
     *
     * @param parent El `ViewGroup` en el que se añadirá la nueva vista.
     * @param viewType El tipo de vista.
     * @return Un nuevo `StockViewHolder` que contiene la vista del elemento.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_stock, parent, false)
        return StockViewHolder(view)
    }

    /**
     * `onBindViewHolder` se llama cuando el `RecyclerView` necesita mostrar los datos en un elemento específico.
     *
     * Obtiene el objeto `StockPrice` correspondiente a la posición y establece los valores de la fecha y el precio de cierre en los `TextView` del `StockViewHolder`.
     *
     * @param holder El `StockViewHolder` que contiene las vistas del elemento.
     * @param position La posición del elemento en la lista.
     */
    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stockList[position]
        holder.date.text = stock.date
        holder.closePrice.text = stock.close.toString()
    }

    /**
     * `getItemCount` devuelve el número total de elementos en la lista.
     *
     * @return El número de elementos en la lista `stockList`.
     */
    override fun getItemCount() = stockList.size
}
