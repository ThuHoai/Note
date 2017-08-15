package com.hoaiutc95.note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    static final String ALARM = "";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "Note.sqlite";
    static final String TABLE_NAME = "Content";
    static final String KEY_ROWID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_ALARM = "alarm";
    static final String KEY_TIME = "time";
    static final String KEY_IMAGE = "image";
    static final String KEY_COLOR = "color";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "( "
            + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_COLOR + " TEXT, "
            + KEY_ALARM + " TEXT, "
            + KEY_TIME + " TEXT, "
            + KEY_IMAGE + " TEXT)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS");
        onCreate(db);
    }
}
