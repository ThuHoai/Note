package com.hoaiutc95.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hoaiutc95.note.model.Note;
import com.hoaiutc95.note.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Thu Hoai on 8/9/2017.
 */

public class DAOdb {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DAOdb(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addNote(Note contact) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_TITLE, contact.getmTitle());
        values.put(DBHelper.KEY_DESCRIPTION, contact.getmDescription());
        values.put(DBHelper.KEY_COLOR, contact.getmColor());
        if (contact.getmAlarm() != null) {
            values.put(DBHelper.KEY_ALARM, contact.getmAlarm());
        } else {
            values.putNull(DBHelper.ALARM);
        }
        values.put(DBHelper.KEY_TIME, contact.getmCreateDate());
        values.put(DBHelper.KEY_IMAGE, contact.getmImagePath());
       return database.insert(DBHelper.TABLE_NAME, null, values);
    }

    public ArrayList<Note> getNotes() {
        ArrayList<Note> lstNote = new ArrayList<>();
        String sql = "select * from content order by time desc";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note myNote = getNoteFromCursor(cursor);
            lstNote.add(myNote);
            cursor.moveToNext();
        }
        cursor.close();
        return lstNote;
    }

    public void upDateNote(Note contact){
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ROWID, contact.getmId());
        values.put(DBHelper.KEY_TITLE, contact.getmTitle());
        values.put(DBHelper.KEY_DESCRIPTION, contact.getmDescription());
        values.put(DBHelper.KEY_COLOR, contact.getmColor());
        if (contact.getmAlarm() != null) {
            values.put(DBHelper.KEY_ALARM, contact.getmAlarm());
        } else {
            values.putNull(DBHelper.ALARM);
        }
        values.put(DBHelper.KEY_TIME, contact.getmCreateDate());
        values.put(DBHelper.KEY_IMAGE, contact.getmImagePath());
        database.update(DBHelper.TABLE_NAME,values, "ID ="+ contact.getmId(),null );
    }

    public void deteleNote(long id) {
        String sql = "DELETE FROM "+ DBHelper.TABLE_NAME + " where id = " + id;
      database.execSQL(sql);
        close();
    }


    public Note getNoteFromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROWID));
        String title = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION));
        String createDate = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIME));
        String alarm = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ALARM));
        try {
            return new Note(Long.parseLong(id), title, description, cursor.getString(cursor.getColumnIndex(DBHelper.KEY_COLOR)), alarm != null ? alarm : "",createDate,cursor.getString(cursor.getColumnIndex(DBHelper.KEY_IMAGE)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }


}
