package com.example.maclassesdufutur;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlLite extends SQLiteOpenHelper {

    private String creation= "create table taches ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "nameActivite TEXT NOT NULL,"
            + "nameTache TEXT NOT NULL,"
            + "status TEXT NOT NULL,"
            + "time INTEGER NOT NULL);";

    public SqlLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
