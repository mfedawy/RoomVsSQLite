package com.mzdhr.roomvssqlite.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "SQLiteNoteDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // SQL COMMANDS
    private String SQL_CREATE_NOTE_TABLE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SQL_CREATE_NOTE_TABLE = "CREATE TABLE " + DatabaseContract.NoteEntry.NOTE_TABLE_NAME + " ("
                + DatabaseContract.NoteEntry._ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.NoteEntry.COLUMN_NOTE_TITLE +  " TEXT NOT NULL, "
                + DatabaseContract.NoteEntry.COLUMN_NOTE_BODY +  " TEXT NOT NULL DEFAULT '' )";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
