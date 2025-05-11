package com.example.app_trading.java;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
public class UserDao {


    private final DatabaseHelper dbHelper;

    public UserDao(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public void insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, user.getId());
        values.put(DatabaseHelper.COLUMN_NOMBRE, user.getNombre());
        values.put(DatabaseHelper.COLUMN_APELLIDO, user.getApellido());
        values.put(DatabaseHelper.COLUMN_APELLIDO2, user.getApellido2());
        values.put(DatabaseHelper.COLUMN_GMAIL, user.getGmail());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, user.getImageUrl());
        values.put(DatabaseHelper.COLUMN_FECHA_NAZ, user.getFechaNaz());
        values.put(DatabaseHelper.COLUMN_FECHA_UPDATE, user.getFechaUpdate());
        values.put(DatabaseHelper.COLUMN_DINERO, user.getDinero());
        values.put(DatabaseHelper.COLUMN_ID_ACCION, user.getIdAccion());
        db.insert(DatabaseHelper.TABLE_USER, null, values);
        db.close();
    }

    public User getUserById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, null, DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{id}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APELLIDO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APELLIDO2)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FECHA_NAZ)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FECHA_UPDATE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DINERO)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID_ACCION))
            );
            cursor.close();
            db.close();
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER, null);
        if (cursor.moveToFirst()) {
            do {
                users.add(new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APELLIDO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APELLIDO2)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FECHA_NAZ)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FECHA_UPDATE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DINERO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID_ACCION))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOMBRE, user.getNombre());
        values.put(DatabaseHelper.COLUMN_APELLIDO, user.getApellido());
        values.put(DatabaseHelper.COLUMN_APELLIDO2, user.getApellido2());
        values.put(DatabaseHelper.COLUMN_GMAIL, user.getGmail());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, user.getImageUrl());
        values.put(DatabaseHelper.COLUMN_FECHA_NAZ, user.getFechaNaz());
        values.put(DatabaseHelper.COLUMN_FECHA_UPDATE, user.getFechaUpdate());
        values.put(DatabaseHelper.COLUMN_DINERO, user.getDinero());
        values.put(DatabaseHelper.COLUMN_ID_ACCION, user.getIdAccion());
        //db.update(DatabaseHelper.TABLE_USER, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{user.getId()});
        db.close();
    }

    public void deleteUser(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_USER, DatabaseHelper.COLUMN_ID + " = ?", new String[]{id});
        db.close();
    }
}
