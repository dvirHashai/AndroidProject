package com.cambio.finalprojectandroid.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dvirh on 8/24/2017.
 */

public class ModelSql extends SQLiteOpenHelper {
    ModelSql(Context context) {
        super(context, "database.db", null, 9);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        EventSql.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        EventSql.onUpgrade(db, oldVersion, newVersion);
    }


}
