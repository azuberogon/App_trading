package com.example.app_trading

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_trading.kotlin.Adapter.CustomAdapter
import com.example.app_trading.kotlin.Model.busquedasEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import android.os.Handler
import android.os.Looper

import com.example.app_trading.kotlin.extras.GraficasTest1

import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * [MainActivity] es la pantalla principal de la aplicación.
 * Permite al usuario buscar activos (acciones, etc.) a través de una API
 * y muestra los resultados en un RecyclerView. Al seleccionar un activo,
 * navega a una pantalla de gráficos ([com.example.app_trading.kotlin.extras.GraficasTest1]).
 */
class MainActivity : AppCompatActivity() {

    // --- Propiedades de la clase ---

    /**
     * Lista mutable que almacena la lista completa de resultados de búsqueda
     * obtenidos de la API.
     */
    private val fullList = mutableListOf<busquedasEntity>()

    /**
     * Adaptador para el RecyclerView que gestiona la visualización de los
     * elementos de búsqueda.
     */
    private lateinit var recyclerAdapter: CustomAdapter

    /**
     * [Handler] asociado al hilo principal (MainLooper) para gestionar el
     * retraso en la ejecución de la búsqueda (debounce).
     */
    private val handler = Handler(Looper.getMainLooper())

    /**
     * [Runnable] que contiene la lógica de búsqueda. Se utiliza para
     * implementar la funcionalidad de debounce en el campo de búsqueda.
     */
    private var searchRunnable: Runnable? = null

    // --- Métodos de la clase ---

    /**
     * Método que se llama cuando se crea la actividad por primera vez.
     * Configura la interfaz de usuario, el RecyclerView y el listener
     * para el campo de búsqueda.
     *
     * @param savedInstanceState Si la actividad se está recreando
     * de un estado guardado previamente, este es el estado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Establece el layout para esta actividad

        // Obtiene referencias a las vistas del layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.barra_de_navegacionMain)

        // --- Configuración del RecyclerView ---
        recyclerView.layoutManager = LinearLayoutManager(this) // Establece un LayoutManager lineal
        // Inicializa el adaptador con la lista y un listener para clics en elementos
        recyclerAdapter = CustomAdapter(fullList) { selectedItem ->
            // Acción al hacer clic en un elemento: iniciar la actividad de gráficos
            val intent = Intent(this, GraficasTest1::class.java)
            intent.putExtra("name", selectedItem.name) // Pasa el nombre del activo
            intent.putExtra("ticker", selectedItem.ticker) // Pasa el ticker del activo
            startActivity(intent) // Inicia la nueva actividad
        }
        recyclerView.adapter = recyclerAdapter // Asigna el adaptador al RecyclerView

        // Añade un ItemAnimator por defecto para animaciones al añadir/eliminar elementos
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        // Añade un decorador para mostrar líneas divisorias entre elementos
        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        // --- Configuración del campo de búsqueda (AutoCompleteTextView) ---
        // Añade un TextWatcher para escuchar los cambios en el texto.
        // Se implementa debounce para evitar llamadas excesivas a la API.
        searchView.addTextChangedListener(object : TextWatcher {
            /**
             * Se llama antes de que el texto cambie.
             */
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            /**
             * Se llama cuando el texto ha cambiado.
             * Implementa la lógica de debounce.
             */
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Cancela cualquier Runnable de búsqueda anterior pendiente
                searchRunnable?.let { handler.removeCallbacks(it) }
                // Crea un nuevo Runnable con la lógica de búsqueda
                searchRunnable = Runnable {
                    // Solo realiza la búsqueda si el texto no está vacío y tiene al menos 2 caracteres
                    if (!s.isNullOrEmpty() && s.length >= 2) {
                        fetchDataFromApi(s.toString()) { busquedasList, error ->
                            // Actualiza la UI en el hilo principal (Main thread)
                            runOnUiThread {
                                if (error != null) {
                                    // Muestra un mensaje de error si la llamada a la API falla
                                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                                } else {
                                    // Limpia la lista actual, añade los nuevos resultados y notifica al adaptador
                                    fullList.clear()
                                    fullList.addAll(busquedasList)
                                    recyclerAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    } else {
                        // Si el texto es vacío o tiene menos de 2 caracteres, limpia la lista y el RecyclerView
                        fullList.clear()
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
                // Programa la ejecución del Runnable después de 500ms (debounce)
                handler.postDelayed(searchRunnable!!, 500)
            }

            /**
             * Se llama después de que el texto ha cambiado.
             */
            override fun afterTextChanged(s: Editable?) {}
        })

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inversiones -> {
                    startActivity(Intent(this, MisInversiones::class.java))
                    true
                }
                R.id.navigation_noticias -> {
                    startActivity(Intent(this, Noticias::class.java))
                    true
                }
                R.id.navigation_busqueda -> {
                    // Ya estamos en MainActivity, no hacemos nada
                    true
                }
                R.id.navigation_calculadora -> {
                    startActivity(Intent(this, com.example.app_trading.kotlin.Conversor.conversorDeDivisas::class.java))
                    true
                }
                R.id.navigation_Ajustes -> {
                    startActivity(Intent(this, Ajustes::class.java))
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Realiza una llamada a la API de Tiingo para buscar activos basados en una consulta.
     * La llamada se realiza en un hilo secundario para no bloquear el hilo principal.
     *
     * @param query La cadena de búsqueda introducida por el usuario.
     * @param callback Una función de callback que se llama al completar la llamada a la API.
     *                   Recibe la lista de resultados ([List]<[busquedasEntity]>) y un mensaje de error ([String]?)
     *                   (null si no hay error).
     */
    private fun fetchDataFromApi(query: String, callback: (List<busquedasEntity>, String?) -> Unit) {
        val client = OkHttpClient() // Cliente HTTP para realizar la petición
        // Construye la URL de la API con la consulta y el token
        val url = "https://api.tiingo.com/tiingo/utilities/search?query=$query&token=${Api.TOKEN_TINGO}"
        // Construye la petición HTTP GET
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json") // Añade la cabecera Content-Type
            .build()

        // Ejecuta la petición en un nuevo hilo
        Thread {
            try {
                // Ejecuta la llamada HTTP y espera la respuesta
                val response = client.newCall(request).execute()
                // Comprueba si la respuesta fue exitosa (código 2xx)
                if (response.isSuccessful) {
                    // Obtiene el cuerpo de la respuesta como una cadena
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        // Define el tipo de dato esperado para Gson (una lista de busquedasEntity)
                        val listType = object : TypeToken<List<busquedasEntity>>() {}.type
                        // Deserializa la respuesta JSON a una lista de objetos busquedasEntity
                        val busquedasList: List<busquedasEntity> = Gson().fromJson(responseData, listType)
                        callback(busquedasList, null) // Llama al callback con la lista de resultados y sin error
                    } else {
                        callback(emptyList(), "Respuesta vacía de la API") // Llama al callback con un error si la respuesta está vacía
                    }
                } else {
                    callback(emptyList(), "Error en la API: ${response.code}") // Llama al callback con un error si la respuesta no fue exitosa
                }
            } catch (e: Exception) {
                // Captura cualquier excepción durante la llamada (ej. error de red)
                e.printStackTrace() // Imprime la traza del error en la consola
                callback(emptyList(), "Error de red: ${e.message}")
            }
        }.start()
    }
}