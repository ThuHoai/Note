package com.hoaiutc95.note.activity;

import android.app.AlarmManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.hoaiutc95.note.R;
import com.hoaiutc95.note.custom.NoteItemAdapter;
import com.hoaiutc95.note.database.DAOdb;
import com.hoaiutc95.note.model.Note;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Note> mListNote = new ArrayList<>();
    private NoteItemAdapter mAdapter;
    private DAOdb mDatabase;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = new DAOdb(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mGridView = (GridView) findViewById(R.id.gvListNote);
        mListNote = new ArrayList<>();
        mListNote = mDatabase.getNotes();
        mAdapter = new NoteItemAdapter(this, R.layout.gridview_item_note, mListNote);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mGridView.setOnItemClickListener(new NoteItemClick());
    }

    private class NoteItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, DetailNoteActivity.class);
            intent.putExtra("Note", mListNote);
            intent.putExtra("possition", position);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_action_new) {
            Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }
}
