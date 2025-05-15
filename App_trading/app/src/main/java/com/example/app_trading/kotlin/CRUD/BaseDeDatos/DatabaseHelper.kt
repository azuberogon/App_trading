package com.example.app_trading.kotlin.CRUD.BaseDeDatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.app_trading.kotlin.CRUD.Entity.Accion
import com.example.app_trading.kotlin.CRUD.Entity.Inversion
import com.example.app_trading.kotlin.CRUD.Entity.User
import com.google.firebase.firestore.FirebaseFirestore

/**
* CursorFactory es un objeto que te permitiría personalizar cómo se crean los objetos Cursor.
 * Los objetos Cursor son utilizados para iterar sobre los resultados de una consulta a la base
 * de datos (es decir, las filas que devuelve la consulta).

 * Clase auxiliar para gestionar la creación y gestión de la base de datos SQLite local
 * para la aplicación de trading. Extiende [SQLiteOpenHelper] para manejar la
 * creación y actualización de la base de datos.
 *
 * También incluye funcionalidad para interactuar con Firestore para la obtención
 * de datos de usuarios y acciones.
 *
 * @param context El contexto de la aplicación.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "MyDatabase.db"
        private const val DATABASE_VERSION = 1

        // Constantes para la tabla de usuarios
        private const val TABLE_USER = "User"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_APELLIDO = "apellido"
        private const val COLUMN_APELLIDO2 = "apellido2"
        private const val COLUMN_GMAIL = "gmail"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_IMAGE_URL = "imageUrl"
        private const val COLUMN_FECHA_NAZ = "fechaNaz"
        private const val COLUMN_FECHA_UPDATE = "fechaUpdate"
        private const val COLUMN_DINERO = "dinero"
        private const val COLUMN_ID_ACCION = "idAccion"

        // Constantes para la tabla de acciones (aunque los nombres de columna están definidos en el CREATE TABLE)
        // Sería buena práctica definirlas aquí también si se usan en otras funciones de la clase.
        // Ejemplo:
        // private const val TABLE_ACCION = "Accion"
        // private const val COLUMN_ACCION_ID = "id"
        // ...
    }

    /**
     * Llamado cuando la base de datos se crea por primera vez. Este es el lugar
     * donde se deben crear las tablas y los datos iniciales.
     *
     * @param db La base de datos SQLite que se está creando.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        // Sentencia SQL para crear la tabla de usuarios
        val createUserTable = """
        CREATE TABLE $TABLE_USER (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NOMBRE VARCHAR(30),
            $COLUMN_APELLIDO VARCHAR(30),
            $COLUMN_APELLIDO2 VARCHAR(30),
            $COLUMN_GMAIL VARCHAR(50),
            $COLUMN_PASSWORD VARCHAR(50),
            $COLUMN_IMAGE_URL TEXT,
            $COLUMN_FECHA_NAZ DATE,
            $COLUMN_FECHA_UPDATE DATETIME,
            $COLUMN_DINERO REAL,
            $COLUMN_ID_ACCION INTEGER
        );
    """.trimIndent()

        // Sentencia SQL para crear la tabla de acciones con una clave foránea referenciando a la tabla de usuarios
        val createAccionTable = """
        CREATE TABLE Accion (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre VARCHAR(50),
            ticker VARCHAR(10),
            sector VARCHAR(50),
            pais VARCHAR(50),
            divisa VARCHAR(10),
            fechaCreacion DATETIME,
            fechaUpdate DATETIME,
            precioAccion REAL,
            precioCompra REAL,
            cantidadAcciones INTEGER,
            idUser INTEGER,
            FOREIGN KEY (idUser) REFERENCES $TABLE_USER($COLUMN_ID)
        );
    """.trimIndent()

        // Ejecutar las sentencias SQL para crear las tablas
        db?.execSQL(createUserTable)
        db?.execSQL(createAccionTable)
    }

    /**
     * Llamado cuando la base de datos necesita ser actualizada. La implementación
     * debería utilizar sentencias ALTER TABLE para actualizar el esquema de la
     * base de datos de la versión antigua a la nueva versión.
     *
     * @param db La base de datos SQLite que se va a actualizar.
     * @param oldVersion El número de versión antiguo de la base de datos.
     * @param newVersion El número de versión nuevo de la base de datos.
     */
    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        // TODO: Implementar la lógica de actualización de la base de datos si se incrementa DATABASE_VERSION
        TODO("Not yet implemented")
    }

    /**
     * Obtiene los datos de un usuario específico y sus acciones asociadas desde Firestore.
     * Esta función es asíncrona debido a la naturaleza de las operaciones de red.
     *
     * @param userId El ID del usuario cuyos datos se desean obtener.
     * @param onComplete Una función lambda que se llama cuando la operación se completa.
     *                   Recibe un objeto [User] (puede ser null si no se encuentra el usuario)
     *                   y una [List] de [Accion] (puede estar vacía si no hay acciones o hay un error).
     */
    fun fetchUserDataFromFirebase(userId: String, onComplete: (User?, List<Accion>) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()

        // Obtener datos del usuario desde la colección "users" en Firestore
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { userSnapshot ->
                val user = userSnapshot.toObject(User::class.java)

                // Obtener las acciones asociadas a este usuario desde la colección "acciones"
                firestore.collection("acciones")
                    .whereEqualTo("idUser", userId) // Filtra acciones por el ID del usuario
                    .get()
                    .addOnSuccessListener { accionesSnapshot ->
                        // Mapear los documentos de acciones a objetos Accion
                        val acciones = accionesSnapshot.toObjects(Accion::class.java)
                        // Llamar al callback onComplete con los datos del usuario y sus acciones
                        onComplete(user, acciones)
                    }
                    .addOnFailureListener { e ->
                        // Manejar errores al obtener las acciones
                        e.printStackTrace()
                        onComplete(null, emptyList()) // Llamar al callback con null para el usuario y lista vacía para acciones
                    }
            }
            .addOnFailureListener { e ->
                // Manejar errores al obtener los datos del usuario
                e.printStackTrace()
                onComplete(null, emptyList()) // Llamar al callback con null para el usuario y lista vacía para acciones
            }
    }

    /**
     * Inserta una nueva acción en la tabla 'Accion' de la base de datos local.
     *
     * @param nombre El nombre de la acción.
     * @param ticker El símbolo ticker de la acción.
     * @param sector El sector al que pertenece la acción.
     * @param pais El país de la acción.
     * @param divisa La divisa en la que cotiza la acción.
     * @param fechaCreacion La fecha de creación del registro de la acción.
     * @param fechaUpdate La fecha de la última actualización del registro de la acción.
     * @param precioAccion El precio actual de la acción.
     * @param precioCompra El precio al que se compró la acción.
     * @param cantidadAcciones La cantidad de acciones compradas.
     * @param idUser El ID del usuario al que pertenece esta acción (clave foránea).
     * @return El ID de la nueva fila insertada, o -1 si ocurrió un error.
     */
    fun insertAccion(
        nombre: String,
        ticker: String,
        sector: String,
        pais: String,
        divisa: String,
        fechaCreacion: String,
        fechaUpdate: String,
        precioAccion: Double,
        precioCompra: Double,
        cantidadAcciones: Int,
        idUser: Int
    ): Long {
        val db = this.writableDatabase // Obtiene una instancia de la base de datos en modo escritura
        val contentValues = ContentValues() // Objeto para almacenar los valores a insertar

        // Poner los valores en el objeto ContentValues
        contentValues.put("nombre", nombre)
        contentValues.put("ticker", ticker)
        contentValues.put("sector", sector)
        contentValues.put("pais", pais)
        contentValues.put("divisa", divisa)
        contentValues.put("fechaCreacion", fechaCreacion)
        contentValues.put("fechaUpdate", fechaUpdate)
        contentValues.put("precioAccion", precioAccion)
        contentValues.put("precioCompra", precioCompra)
        contentValues.put("cantidadAcciones", cantidadAcciones)
        contentValues.put("idUser", idUser)

        // Insertar la nueva fila en la tabla 'Accion'
        return db.insert("Accion", null, contentValues)
    }

    /**
     * Inserta un nuevo usuario en la tabla 'User' de la base de datos local.
     *
     * @param nombre El nombre del usuario.
     * @param apellido El primer apellido del usuario.
     * @param apellido2 El segundo apellido del usuario.
     * @param gmail El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param imageUrl La URL de la imagen de perfil del usuario.
     * @param fechaNaz La fecha de nacimiento del usuario.
     * @param fechaUpdate La fecha de la última actualización del registro del usuario.
     * @param dinero La cantidad de dinero disponible del usuario.
     * @param idAccion El ID de una acción asociada al usuario (puede ser null si no tiene acciones asociadas).
     * @return El ID de la nueva fila insertada, o -1 si ocurrió un error.
     */
    fun insertUser(
        nombre: String,
        apellido: String,
        apellido2: String,
        gmail: String,
        password: String,
        imageUrl: String,
        fechaNaz: String,
        fechaUpdate: String,
        dinero: Double,
        idAccion: Int?
    ): Long {
        val db = this.writableDatabase // Obtiene una instancia de la base de datos en modo escritura
        val contentValues = ContentValues() // Objeto para almacenar los valores a insertar

        // Poner los valores en el objeto ContentValues
        contentValues.put("nombre", nombre)
        contentValues.put("apellido", apellido)
        contentValues.put("apellido2", apellido2)
        contentValues.put("gmail", gmail)
        contentValues.put("password", password)
        contentValues.put("imageUrl", imageUrl)
        contentValues.put("fechaNaz", fechaNaz)
        contentValues.put("fechaUpdate", fechaUpdate)
        contentValues.put("dinero", dinero)
        contentValues.put("idAccion", idAccion) // Puede ser null

        // Insertar la nueva fila en la tabla 'User'
        return db.insert("User", null, contentValues)
    }

    /**
     * Obtiene todos los usuarios de la tabla 'User' de la base de datos local.
     *
     * @return Una [List] de objetos [User] que representa todos los usuarios en la base de datos.
     *         La lista estará vacía si no hay usuarios o si ocurre un error.
     */
    fun getAllUsers(): List<User> {
        val db = this.readableDatabase // Obtiene una instancia de la base de datos en modo lectura
        // Ejecuta una consulta SQL para seleccionar todos los registros de la tabla 'User'
        val cursor = db.rawQuery("SELECT * FROM User", null)
        val users = mutableListOf<User>() // Lista mutable para almacenar los objetos User

        // Itera sobre los resultados del cursor si hay alguna fila
        if (cursor.moveToFirst()) {
            do {
                // Crea un objeto User a partir de los datos de la fila actual del cursor
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                    apellido2 = cursor.getString(cursor.getColumnIndexOrThrow("apellido2")),
                    gmail = cursor.getString(cursor.getColumnIndexOrThrow("gmail")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")),
                    fechaNaz = cursor.getString(cursor.getColumnIndexOrThrow("fechaNaz")),
                    fechaUpdate = cursor.getString(cursor.getColumnIndexOrThrow("fechaUpdate")),
                    dinero = cursor.getDouble(cursor.getColumnIndexOrThrow("dinero")),
                    idAccion = if (cursor.isNull(cursor.getColumnIndexOrThrow("idAccion"))) null else cursor.getInt(cursor.getColumnIndexOrThrow("idAccion"))
                )
                users.add(user) // Añade el objeto User a la lista
            } while (cursor.moveToNext()) // Continúa mientras haya más filas
        }
        cursor.close() // Cierra el cursor para liberar recursos
        return users // Devuelve la lista de usuarios
    }



    fun getAllInversiones(): List<Inversion> {
        val db = this.readableDatabase
        val inversiones = mutableListOf<Inversion>()
        val cursor = db.rawQuery("SELECT id, nombre, cantidadAcciones, fechaCreacion FROM Accion", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidadAcciones")).toDouble()
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fechaCreacion"))
                inversiones.add(Inversion(id, nombre, cantidad, fecha))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return inversiones
    }
    fun poblarDatosDemo() {
        val db = this.readableDatabase
        // Comprobar si ya hay usuarios
        val cursor = db.rawQuery("SELECT COUNT(*) FROM User", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        if (count == 0) {
            // Insertar usuarios demo
            val idUser1 = insertUser(
                nombre = "Juan",
                apellido = "Pérez",
                apellido2 = "García",
                gmail = "juan.perez@gmail.com",
                password = "123456",
                imageUrl = "",
                fechaNaz = "1990-01-01",
                fechaUpdate = "2024-06-01",
                dinero = 10000.0,
                idAccion = null
            ).toInt()
            val idUser2 = insertUser(
                nombre = "Ana",
                apellido = "López",
                apellido2 = "Martínez",
                gmail = "ana.lopez@gmail.com",
                password = "abcdef",
                imageUrl = "",
                fechaNaz = "1985-05-10",
                fechaUpdate = "2024-06-01",
                dinero = 15000.0,
                idAccion = null
            ).toInt()

            // Insertar acciones demo para cada usuario
            insertAccion(
                nombre = "Apple Inc.",
                ticker = "AAPL",
                sector = "Tecnología",
                pais = "EEUU",
                divisa = "USD",
                fechaCreacion = "2024-05-15",
                fechaUpdate = "2024-05-15",
                precioAccion = 180.0,
                precioCompra = 170.0,
                cantidadAcciones = 10,
                idUser = idUser1
            )
            insertAccion(
                nombre = "Santander",
                ticker = "SAN",
                sector = "Banca",
                pais = "España",
                divisa = "EUR",
                fechaCreacion = "2024-05-10",
                fechaUpdate = "2024-05-10",
                precioAccion = 3.5,
                precioCompra = 3.2,
                cantidadAcciones = 50,
                idUser = idUser1
            )
            insertAccion(
                nombre = "Tesla",
                ticker = "TSLA",
                sector = "Automoción",
                pais = "EEUU",
                divisa = "USD",
                fechaCreacion = "2024-05-12",
                fechaUpdate = "2024-05-12",
                precioAccion = 700.0,
                precioCompra = 650.0,
                cantidadAcciones = 5,
                idUser = idUser2
            )
            insertAccion(
                nombre = "BBVA",
                ticker = "BBVA",
                sector = "Banca",
                pais = "España",
                divisa = "EUR",
                fechaCreacion = "2024-05-20",
                fechaUpdate = "2024-05-20",
                precioAccion = 6.0,
                precioCompra = 5.5,
                cantidadAcciones = 30,
                idUser = idUser2
            )
        }

    }
    fun actualizarCantidadAcciones(idAccion: Int, nuevaCantidad: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("cantidadAcciones", nuevaCantidad)
        db.update("Accion", values, "id = ?", arrayOf(idAccion.toString()))
    }

    fun actualizarDineroUsuario(idUser: Int, nuevoDinero: Double) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("dinero", nuevoDinero)
        db.update("User", values, "id = ?", arrayOf(idUser.toString()))
    }


    fun getAccionById(id: Int): Accion? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Accion WHERE id = ?", arrayOf(id.toString()))
        var accion: Accion? = null
        if (cursor.moveToFirst()) {
            accion = Accion(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                ticker = cursor.getString(cursor.getColumnIndexOrThrow("ticker")),
                sector = cursor.getString(cursor.getColumnIndexOrThrow("sector")),
                pais = cursor.getString(cursor.getColumnIndexOrThrow("pais")),
                divisa = cursor.getString(cursor.getColumnIndexOrThrow("divisa")),
                fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("fechaCreacion")),
                fechaUpdate = cursor.getString(cursor.getColumnIndexOrThrow("fechaUpdate")),
                precioAccion = cursor.getDouble(cursor.getColumnIndexOrThrow("precioAccion")),
                precioCompra = cursor.getDouble(cursor.getColumnIndexOrThrow("precioCompra")),
                cantidadAcciones = cursor.getInt(cursor.getColumnIndexOrThrow("cantidadAcciones")),
                idUser = cursor.getInt(cursor.getColumnIndexOrThrow("idUser"))
            )
        }
        cursor.close()
        return accion
    }

    fun borrarBaseDeDatos() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM Accion")
        db.execSQL("DELETE FROM User")
        // Si tienes más tablas, agrégalas aquí
    }
}