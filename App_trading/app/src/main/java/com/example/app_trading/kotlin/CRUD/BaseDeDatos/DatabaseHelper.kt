package com.example.app_trading.kotlin.CRUD.BaseDeDatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.app_trading.kotlin.CRUD.Entity.User
/**
* CursorFactory es un objeto que te permitiría personalizar cómo se crean los objetos Cursor.
 * Los objetos Cursor son utilizados para iterar sobre los resultados de una consulta a la base
 * de datos (es decir, las filas que devuelve la consulta).
* */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MyDatabase.db"
        private const val DATABASE_VERSION = 1

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
    }

    override fun onCreate(db: SQLiteDatabase?) {
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

        db?.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    // FUNCIONES DEL CRUD:

    // Insertar un nuevo usuario
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
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COLUMN_NOMBRE, nombre)
        contentValues.put(COLUMN_APELLIDO, apellido)
        contentValues.put(COLUMN_APELLIDO2, apellido2)
        contentValues.put(COLUMN_GMAIL, gmail)
        contentValues.put(COLUMN_PASSWORD, password)
        contentValues.put(COLUMN_IMAGE_URL, imageUrl)
        contentValues.put(COLUMN_FECHA_NAZ, fechaNaz)
        contentValues.put(COLUMN_FECHA_UPDATE, fechaUpdate)
        contentValues.put(COLUMN_DINERO, dinero)
        contentValues.put(COLUMN_ID_ACCION, idAccion)

        return db.insert(TABLE_USER, null, contentValues)
    }

    // Leer todos los usuarios
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER", null)

        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO2)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_NAZ)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_UPDATE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DINERO)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_ACCION))
                )
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return userList
    }

    // Actualizar un usuario
    fun updateUser(id: Int, nombre: String, gmail: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NOMBRE, nombre)
        contentValues.put(COLUMN_GMAIL, gmail)

        return db.update(TABLE_USER, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
    /**
     * Busca un usuario en la base de datos por su ID.
     *
     * @param id El ID del usuario que se desea buscar.
     * @return Un objeto `User` si se encuentra un usuario con el ID especificado,
     *         o `null` si no se encuentra ningún usuario.
     */
    fun isGmailRegistered(gmail: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USER, // Nombre de la tabla
            arrayOf(COLUMN_GMAIL), // Columna que queremos verificar
            "$COLUMN_GMAIL = ?", // Cláusula WHERE: busca por Gmail
            arrayOf(gmail), // Argumento para la cláusula WHERE
            null, // Agrupación
            null, // Filtro de grupo
            null  // Orden
        )

        val isRegistered = cursor.moveToFirst() // Verifica si hay resultados
        cursor.close()
        return isRegistered
    }

    fun getUserById(id: Int): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USER, // Nombre de la tabla
            arrayOf( // Columnas que se quieren recuperar
                COLUMN_ID,
                COLUMN_NOMBRE,
                COLUMN_APELLIDO,
                COLUMN_APELLIDO2,
                COLUMN_GMAIL,
                COLUMN_PASSWORD,
                COLUMN_IMAGE_URL,
                COLUMN_FECHA_NAZ,
                COLUMN_FECHA_UPDATE,
                COLUMN_DINERO,
                COLUMN_ID_ACCION
            ),
            "$COLUMN_ID = ?", // Cláusula WHERE: busca por ID
            arrayOf(id.toString()), // Argumentos para la cláusula WHERE (el ID)
            null, // Agrupación (no se usa)
            null, // Filtro de grupo (no se usa)
            null  // Orden (no se usa)
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO2)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_NAZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_UPDATE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DINERO)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_ACCION))
            )
        }
        cursor.close()
        return user
    }






    // Eliminar un usuario
    fun deleteUser(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_USER, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }























































}
