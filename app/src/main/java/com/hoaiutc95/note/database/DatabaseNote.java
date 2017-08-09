package com.hoaiutc95.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.hoaiutc95.note.model.Note;

/**
 * Created by Thu Hoai on 8/3/2017.
 */

public class DatabaseNote extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "Note.sqlite";
    private static final String TABLE_NAME = "Content";
    private static final String KEY_ROWID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_IMAGE = "image";

    private SQLiteDatabase db;

    public static final String DATABASE_CREATE =
            "CREATE TABLE CONTENT ( " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT NOT NULL," + KEY_DESCRIPTION + " TEXT NOT NULL, "
            + KEY_DATE + " TEXT NOT NULL, " + KEY_TIME + " TEXT NOT NULL, " + KEY_IMAGE + " BLOB)";

    public DatabaseNote(Context context) {
        super(context, DATABASE_NAME, null, 1);
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

/*
    public void addNote(Note contact){
        db = this.getWritableDatabase();
        String sql = "INSERT INTO CONTENT VALUES (NULL,?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, contact.getTitle());
        statement.bindString(2, contact.getDescription());
        statement.bindString(3,contact.getDateCurrent());
        statement.bindString(4, contact.getTimeCurrent());
        statement.bindBlob(5,contact.getImage());

        statement.executeInsert();
    }
*/

}
