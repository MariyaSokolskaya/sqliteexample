package com.example.sqliteexample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myBudget.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "Budget";
    public static final String BUDGET_ID = "_id";
    public static final String BUDGET_PURCHASE = "purchase";
    public static final String BUDGET_MONEY = "money";
    public static final String BUDGET_DATE = "date";
    public static final String BUDGET_COMMENT = "comment";

    public OpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + DATABASE_TABLE +
                "(" + BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BUDGET_PURCHASE + " TEXT, " +
                BUDGET_DATE + " TEXT, " +
                BUDGET_MONEY + " REAL, " +
                BUDGET_COMMENT + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
