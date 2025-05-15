package com.example.app_trading.kotlin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.R
import coil.load
import com.example.app_trading.kotlin.CRUD.Entity.Noticia
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NoticiasAdapter(
    private val noticias: List<Noticia>,
    private val onItemClick: (Noticia) -> Unit
) : RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder>() {

    class NoticiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewNoticia: ImageView = itemView.findViewById(R.id.imageViewNoticia)
        val textViewHeadline: TextView = itemView.findViewById(R.id.textViewHeadline)
        val textViewDatetime: TextView = itemView.findViewById(R.id.textViewDatetime)

        fun bind(noticia: Noticia, onItemClick: (Noticia) -> Unit) {
            imageViewNoticia.load(noticia.image) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
            textViewHeadline.text = noticia.headline

            val date = Date(noticia.datetime * 1000)
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()
            textViewDatetime.text = formatter.format(date)

            itemView.setOnClickListener {
                onItemClick(noticia)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_noticia, parent, false)
        return NoticiaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        val noticia = noticias[position]
        holder.bind(noticia, onItemClick)
    }

    override fun getItemCount(): Int = noticias.size
}