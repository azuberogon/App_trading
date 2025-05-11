package com.example.app_trading.kotlin.CRUD.BaseDeDatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.app_trading.kotlin.CRUD.Entity.Accion
import com.example.app_trading.kotlin.CRUD.Entity.User
import com.google.firebase.firestore.FirebaseFirestore

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

//    override fun onCreate(db: SQLiteDatabase?) {
//        val createUserTable = """
//            CREATE TABLE $TABLE_USER (
//                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
//                $COLUMN_NOMBRE VARCHAR(30),
//                $COLUMN_APELLIDO VARCHAR(30),
//                $COLUMN_APELLIDO2 VARCHAR(30),
//                $COLUMN_GMAIL VARCHAR(50),
//                $COLUMN_PASSWORD VARCHAR(50),
//                $COLUMN_IMAGE_URL TEXT,
//                $COLUMN_FECHA_NAZ DATE,
//                $COLUMN_FECHA_UPDATE DATETIME,
//                $COLUMN_DINERO REAL,
//                $COLUMN_ID_ACCION INTEGER
//            );
//        """.trimIndent()
//
//        db?.execSQL(createUserTable)
//    }


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

        db?.execSQL(createUserTable)
        db?.execSQL(createAccionTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }


    fun fetchUserDataFromFirebase(userId: String, onComplete: (User?, List<Accion>) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()

        // Obtener datos del usuario
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { userSnapshot ->
                val user = userSnapshot.toObject(User::class.java)

                // Obtener las acciones del usuario
                firestore.collection("acciones")
                    .whereEqualTo("idUser", userId)
                    .get()
                    .addOnSuccessListener { accionesSnapshot ->
                        val acciones = accionesSnapshot.toObjects(Accion::class.java)
                        onComplete(user, acciones)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        onComplete(null, emptyList())
                    }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                onComplete(null, emptyList())
            }
    }
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
        val db = this.writableDatabase
        val contentValues = ContentValues()

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

        return db.insert("Accion", null, contentValues)
    }
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

        contentValues.put("nombre", nombre)
        contentValues.put("apellido", apellido)
        contentValues.put("apellido2", apellido2)
        contentValues.put("gmail", gmail)
        contentValues.put("password", password)
        contentValues.put("imageUrl", imageUrl)
        contentValues.put("fechaNaz", fechaNaz)
        contentValues.put("fechaUpdate", fechaUpdate)
        contentValues.put("dinero", dinero)
        contentValues.put("idAccion", idAccion)

        return db.insert("User", null, contentValues)
    }

    fun getAllUsers(): List<User> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User", null)
        val users = mutableListOf<User>()

        if (cursor.moveToFirst()) {
            do {
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
                    idAccion = cursor.getInt(cursor.getColumnIndexOrThrow("idAccion"))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }

}
