package com.hoaiutc95.note.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hoaiutc95.note.R;
import com.hoaiutc95.note.model.Note;


public class NewNoteActivity extends BaseNoteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_note;
    }

    protected void getView() {
        customActionBar();
        getTimeCurrent();
        mTitle = (EditText) findViewById(R.id.etNewTitle);
        mContent = (EditText) findViewById(R.id.etNewContent);
        mSpAlarmDate = (Spinner) findViewById(R.id.spAlarmDate);
        mSpAlarmTime = (Spinner) findViewById(R.id.spAlarmTime);
        mLayout = (ScrollView) findViewById(R.id.paperLayout);
        mEnableAlarm = (TextView) findViewById(R.id.tvEnableAlarm);
        mCurrentDate = (TextView) findViewById(R.id.tvDateTimeCurrent);
        mAlarmBackLayout = (LinearLayout) findViewById(R.id.llAlarmBackLayout);
        mTvDateTimeCurrent = (TextView) findViewById(R.id.tvDateTimeCurrent);
        mSpAlarmDate.setAdapter(mArrayDateAlarm);
        mSpAlarmTime.setAdapter(mArrayTimeAlarm);
        mTitle.addTextChangedListener(new MyTextWatcher());
        mArrayDateAlarm = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mDateName);
        mArrayTimeAlarm = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mTimeName);
    }

    public void saveNote() {
        String title = mTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        String time = mCurrentDate.getText().toString().trim();
        String alarm = mDate + "" + mTime;
        mDate = "";
        mTime = "";
        String color = mColor != null ? mColor : null;
        String path = convertPictureListtoString();
        Note newNote = new Note(title, content, color, alarm, time, path);
        long i = database.addNote(newNote);
        if (i > 0) {
            Toast.makeText(this, "save thanh cong", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Loi file", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        menu.removeItem(R.id.item_new_note);
        return true;
    }
}

