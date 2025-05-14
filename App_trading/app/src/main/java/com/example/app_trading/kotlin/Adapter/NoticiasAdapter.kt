package com.example.app_trading.kotlin.Adapter // Ajusta el paquete según tu estructura

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.R // Importa tu archivo R
import coil.load // Importa la función load de Coil
import com.example.app_trading.Noticias
import com.example.app_trading.kotlin.CRUD.Entity.Noticia
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Adaptador para el RecyclerView que muestra una lista de noticias.
 * Se encarga de crear las vistas para cada elemento de la lista y vincular los datos a esas vistas.
 *
 * @property noticias La lista de objetos [Noticia] a mostrar.
 * @property onItemClick El listener que se llama cuando se hace clic en un elemento de la lista.
 */
class NoticiasAdapter(
    private val noticias: List<Noticias>,
    private val onItemClick: (Noticia) -> Unit // Lambda para manejar clics en elementos
) : RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder>() {

    /**
     * ViewHolder que contiene las vistas para un solo elemento de la lista (una noticia).
     * Define y obtiene referencias a las vistas dentro de un elemento de la lista.
     *
     * @param itemView La vista raíz del layout del elemento (`item_noticia.xml`).
     */
    class NoticiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewNoticia: ImageView = itemView.findViewById(R.id.imageViewNoticia)
        val textViewHeadline: TextView = itemView.findViewById(R.id.textViewHeadline)
        val textViewDatetime: TextView = itemView.findViewById(R.id.textViewDatetime)

        /**
         * Vincula un objeto [Noticia] a las vistas del ViewHolder.
         * Configura el contenido de las vistas y establece un listener de clic en el elemento completo.
         *
         * @param noticia El objeto [Noticia] a mostrar.
         * @param onItemClick La función de callback para manejar el clic.
         */
        fun bind(noticia: Noticia, onItemClick: (Noticia) -> Unit) {
            // Carga la imagen desde la URL usando Coil.
            // Coil es una librería para cargar imágenes de forma eficiente en Android.
            // Si la URL de la imagen está vacía o es nula, se muestra una imagen de error o placeholder.
            imageViewNoticia.load(noticia.image) {
                crossfade(true) // Animación de fundido cruzado al cargar la imagen.
                placeholder(R.drawable.ic_launcher_background) // Imagen que se muestra mientras carga.
                error(R.drawable.ic_launcher_background) // Imagen que se muestra si falla la carga.
                // También puedes añadir otras opciones como .transformations() o .size().
            }

            // Establece el texto del TextView del titular con el titular de la noticia.
            textViewHeadline.text = noticia.headline

            // Formatea el timestamp Unix (que está en segundos) a un formato de fecha y hora legible.
            val date = Date(noticia.datetime * 1000) // Convierte segundos a milisegundos (Unix timestamp * 1000).
            // Define el formato de salida deseado (Día/Mes/Año Hora:Minuto).
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            // Puedes ajustar la zona horaria si es necesario.
            // TimeZone.getDefault() usa la zona horaria del dispositivo.
            // TimeZone.getTimeZone("UTC") si el timestamp es universal (UTC).
            formatter.timeZone = TimeZone.getDefault()
            // Establece el texto del TextView de la fecha y hora con la fecha formateada.
            textViewDatetime.text = formatter.format(date)

            // Establece un listener de clic en la vista raíz del elemento (itemView).
            // Cuando se hace clic, se llama a la lambda 'onItemClick' proporcionada al adaptador,
            // pasándole el objeto 'noticia' asociado a este ViewHolder.
            itemView.setOnClickListener {
                onItemClick(noticia)
            }
        }
    }

    /**
     * Crea nuevos ViewHolders cuando el RecyclerView necesita uno nuevo.
     * Este método es llamado por el LayoutManager.
     *
     * @param parent El ViewGroup en el que se inflarán las nuevas vistas.
     * @param viewType Un entero que representa el tipo de vista si tu RecyclerView tiene varios tipos de elementos.
     * @return Un nuevo [NoticiaViewHolder] que contiene las vistas para un elemento de la lista.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        // Infla el layout del elemento de la noticia (item_noticia.xml).
        // LayoutInflater.from(parent.context) obtiene un inflador para el contexto correcto.
        // attachToRoot es false porque el RecyclerView añadirá la vista por nosotros.
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_noticia, parent, false)
        // Retorna una nueva instancia de NoticiaViewHolder con la vista inflada.
        return NoticiaViewHolder(itemView)
    }

    /**
     * Reemplaza el contenido de las vistas de un elemento de la lista con los datos
     * del elemento en la posición especificada. Este método es llamado por el LayoutManager.
     *
     * @param holder El [NoticiaViewHolder] que debe ser actualizado.
     * @param position La posición del elemento en la lista de datos.
     */
    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        // Obtiene el objeto Noticia de la lista en la posición actual.
        val noticia = noticias[position]
        // Llama al método bind del ViewHolder para actualizar sus vistas con los datos de la noticia.
        // También pasa la lambda onItemClick para que el ViewHolder pueda manejar los clics.
        holder.bind(noticia, onItemClick)
    }

    /**
     * Retorna el número total de elementos en el conjunto de datos que el adaptador gestiona.
     *
     * @return El número total de noticias en la lista.
     */
    override fun getItemCount(): Int {
        // Retorna el tamaño de la lista de noticias.
        return noticias.size
    }
}