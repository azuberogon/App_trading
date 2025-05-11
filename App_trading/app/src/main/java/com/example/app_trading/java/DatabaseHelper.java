package com.example.app_trading.java;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "NeoTradeDataBase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USER = "User";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDO = "apellido";
    public static final String COLUMN_APELLIDO2 = "apellido2";
    public static final String COLUMN_GMAIL = "gmail";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IMAGE_URL = "imageUrl";
    public static final String COLUMN_FECHA_NAZ = "fechaNaz";
    public static final String COLUMN_FECHA_UPDATE = "fechaUpdate";
    public static final String COLUMN_DINERO = "dinero";
    public static final String COLUMN_ID_ACCION = "idAccion";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_APELLIDO + " TEXT, " +
                COLUMN_APELLIDO2 + " TEXT, " +
                COLUMN_GMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_FECHA_NAZ + " TEXT, " +
                COLUMN_FECHA_UPDATE + " TEXT, " +
                COLUMN_DINERO + " REAL, " +
                COLUMN_ID_ACCION + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }


    /*
    private static final String DATABASE_NAME = "NeoTradeDataBase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_APELLIDO = "apellido";
    private static final String COLUMN_APELLIDO2 = "apellido2";
    private static final String COLUMN_GMAIL = "gmail";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IMAGE_URL = "imageUrl";
    private static final String COLUMN_FECHA_NAZ = "fechaNaz";
    private static final String COLUMN_FECHA_UPDATE = "fechaUpdate";
    private static final String COLUMN_DINERO = "dinero";
    private static final String COLUMN_ID_ACCION = "idAccion";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_APELLIDO + " TEXT, " +
                COLUMN_APELLIDO2 + " TEXT, " +
                COLUMN_GMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_FECHA_NAZ + " TEXT, " +
                COLUMN_FECHA_UPDATE + " TEXT, " +
                COLUMN_DINERO + " REAL, " +
                COLUMN_ID_ACCION + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // Insertar un nuevo usuario
    public long insertUser(String nombre, String apellido, String apellido2, String gmail, String password,
                           String imageUrl, String fechaNaz, String fechaUpdate, double dinero, int idAccion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_APELLIDO, apellido);
        values.put(COLUMN_APELLIDO2, apellido2);
        values.put(COLUMN_GMAIL, gmail);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        values.put(COLUMN_FECHA_NAZ, fechaNaz);
        values.put(COLUMN_FECHA_UPDATE, fechaUpdate);
        values.put(COLUMN_DINERO, dinero);
        values.put(COLUMN_ID_ACCION, idAccion);

        return db.insert(TABLE_USER, null, values);
    }

    // Leer todos los usuarios
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USER, null);
    }

    // Actualizar un usuario
    public int updateUser(int id, String nombre, String apellido, String gmail, double dinero) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_APELLIDO, apellido);
        values.put(COLUMN_GMAIL, gmail);
        values.put(COLUMN_DINERO, dinero);

        return db.update(TABLE_USER, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Eliminar un usuario
    public int deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USER, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }*/
}