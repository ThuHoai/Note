package com.hoaiutc95.note.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hoaiutc95.note.R;
import com.hoaiutc95.note.database.DAOdb;
import com.hoaiutc95.note.model.Note;

import java.text.ParseException;
import java.util.ArrayList;

public class DetailNoteActivity extends BaseNoteActivity {
    private ArrayList<Note> mListNote;
    private Note mNote;
    private String time = "";
    private int mNotePossition;
    private ImageButton mNextBT, mPreBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutId();
        try {
            getView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_note;
    }

    private void getView() throws ParseException {
        customActionBar();
        database = new DAOdb(this);
        mListNote = new ArrayList<>();
        mNote = new Note();
        mAlarmBackLayout = (LinearLayout) findViewById(R.id.llAlarmBackLayout);
        mEnableAlarm = (TextView) findViewById(R.id.tvEnableAlarm);
        mSpAlarmDate = (Spinner) findViewById(R.id.spAlarmDate);
        mSpAlarmTime = (Spinner) findViewById(R.id.spAlarmTime);
        mLayout = (ScrollView) findViewById(R.id.paperLayout);
        mTvDateTimeCurrent = (TextView) findViewById(R.id.tvDateTimeCurrent);
        mTitle = (EditText) findViewById(R.id.etNewTitle);
        mContent = (EditText) findViewById(R.id.etNewContent);
        mCurrentDate = (TextView) findViewById(R.id.tvDateTimeCurrent);
        mGvPicture = (GridView) findViewById(R.id.grvPicture);
        mArrayDateAlarm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mDateName);
        mArrayTimeAlarm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mTimeName);
        mSpAlarmDate.setAdapter(mArrayDateAlarm);
        mSpAlarmTime.setAdapter(mArrayTimeAlarm);
        mNextBT = (ImageButton) findViewById(R.id.ibtn_next);
        mPreBT = (ImageButton) findViewById(R.id.ibtn_previous);
        mTitle.addTextChangedListener(new MyTextWatcher());
        Intent intent = getIntent();
        mListNote = (ArrayList<Note>) intent.getSerializableExtra("Note");
        mNotePossition = intent.getExtras().getInt("possition");
        mGvPicture.setVisibility(View.VISIBLE);
        mNote = mListNote.get(mNotePossition);
        getNote(mNote);
        checkEnableButton();
    }

    @Override
    public void saveNote() {
        String title = mTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        String alarm = isAlarmChanged() ? time : mNote.getmAlarm();
        String newColor = mColor != null ? mColor : mNote.getmColor();
        database.upDateNote(new Note(mNote.getmId(), title, content,
        newColor, alarm, mNote.getmCreateDate(), convertPictureListtoString()));
    }

    private void getNote(Note note) {
        mPictrurePathList.clear();
        mTitle.setText(note.getmTitle());
        mContent.setText(note.getmDescription());
        mCurrentDate.setText(note.getmCreateDate());
        String[] path = note.getmImagePath().split(",");
        for (String item : path) {
            mPictrurePathList.add(item);
        }
        mPictureItemAdapter.notifyDataSetChanged();
        if (!note.getmAlarm().equals("")) {
            mEnableAlarm.setVisibility(View.GONE);
            mAlarmBackLayout.setVisibility(View.VISIBLE);
            mDateName[3] = note.getmAlarm().substring(0, 10);
            mArrayDateAlarm.notifyDataSetChanged();
            mTimeName[4] = note.getmAlarm().substring(10, 15);
            mArrayTimeAlarm.notifyDataSetChanged();
            mSpAlarmDate.setSelection(mDateName.length - 1);
            mPrevPosDateSpinner = mDateName.length - 1;
            mSpAlarmTime.setSelection(mTimeName.length - 1);
            mPrevPostTimeSpinner = mTimeName.length - 1;
            mSpAlarmDate.setOnItemSelectedListener(this.onDateSpinnerItemSelectedListener);
            mSpAlarmTime.setOnItemSelectedListener(this.onTimeSpinnerItemSelectedListener);

        } else {
            mEnableAlarm.setVisibility(View.VISIBLE);
            mAlarmBackLayout.setVisibility(View.GONE);
            mSpAlarmDate.setOnItemSelectedListener(null);
            mSpAlarmTime.setOnItemSelectedListener(null);
        }
        if (note.getmImagePath().equals("")) {
            mGvPicture.setVisibility(View.GONE);
        }
        mLayout.setBackgroundColor(Color.parseColor(note.getmColor()));
        checkEnableButton();
    }

    public void nextNote(View v) {
        int i = mNotePossition + 1;
        mNotePossition = i;
        mNote = mListNote.get(i);
        getNote(mNote);
    }

    public void prevNote(View v) {
        int i = mNotePossition - 1;
        mNotePossition = i;
        mNote = mListNote.get(i);
        getNote(mNote);
    }

    private void checkEnableButton() {
        if (mNotePossition == -1) {
            mPreBT.setEnabled(false);
            mPreBT.setAlpha(0.25f);
            mNextBT.setEnabled(false);
            mNextBT.setAlpha(0.25f);
            return;
        }
        if (mNotePossition == 0) {
            mPreBT.setEnabled(false);
            mPreBT.setAlpha(0.25f);
        } else {
            mPreBT.setEnabled(true);
            mPreBT.setAlpha(1.0f);
        }
        if (this.mNotePossition == (mListNote.size() - 1)) {
            mNextBT.setEnabled(false);
            mNextBT.setAlpha(0.25f);
            return;
        }
        mNextBT.setEnabled(true);
        mNextBT.setAlpha(1.0f);
    }

    public void shareNote(View v) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void deleteNote(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailNoteActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Confirm Delete?");
        alertDialog.setMessage("Are you sure you want to delete this?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                long id = mListNote.get(mNotePossition).getmId();
                database.deteleNote(id);
                startActivity(new Intent(DetailNoteActivity.this, MainActivity.class));
                finish();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        return true;
    }

    private boolean isAlarmChanged() {
        time = mDate + "" + mTime;
        if (time != null) {
            if (time.equals(mNote.getmAlarm())) {
                return false;
            }
            return true;
        } else if (mNote.getmAlarm() == null || mNote.getmAlarm().equals(time)) {
            return false;
        } else {
            return true;
        }
    }
}
