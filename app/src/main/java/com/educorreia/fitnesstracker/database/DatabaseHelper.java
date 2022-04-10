package com.educorreia.fitnesstracker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.educorreia.fitnesstracker.models.RegisterItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DB_NAME = "fitness_tracker.db";
    private static final int DB_VERSION = 1;

    private static DatabaseHelper INSTANCE;

    public static DatabaseHelper getInstance(Context context){
        if(INSTANCE == null)
            INSTANCE = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);

        return INSTANCE;
    }

    private DatabaseHelper(@Nullable Context context, @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE registers (id INTEGER PRIMARY KEY, type TEXT, value DECIMAL, " +
                        "created_at DATETIME);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d(TAG, "onUpgrade triggered");
    }

    public long addRegister(String type, double value){
        SQLiteDatabase db = getWritableDatabase();

        long registerId = 0;
        try{
            db.beginTransaction();

            ContentValues content = new ContentValues();
            content.put("type", type);
            content.put("value", value);
            content.put("created_at", getCurrentDate());

            registerId = db.insertOrThrow("registers", null, content);

            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
        }finally {
            if(db.isOpen())
                db.endTransaction();
        }

        return registerId;
    }

    public List<RegisterItem> listRegistersByType(String type){
        List<RegisterItem> registers = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM registers WHERE type = ?", new String[]{ type });

        try {
            if(cursor.moveToFirst()){
                do {
                    @SuppressLint("Range") double value = cursor.getDouble(cursor.getColumnIndex("value"));
                    @SuppressLint("Range") String createdAt = cursor.getString(cursor.getColumnIndex("created_at"));

                    RegisterItem registerItem = new RegisterItem(type, value, createdAt);

                    registers.add(registerItem);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return registers;
    }

    private String getCurrentDate(){
        Locale locale = new Locale("pt", "BR");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);

        return dateFormat.format(new Date());
    }
}
