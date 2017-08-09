package com.hoaiutc95.note.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hoaiutc95.note.R;

public class NewNoteActivity extends BaseNoteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        customActionBar();
        getView();
    }

    @Override
    public void customActionBar() {
        this.mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            this.mActionBar.setDisplayHomeAsUpEnabled(true);
            this.mActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP|ActionBar.DISPLAY_USE_LOGO|ActionBar.DISPLAY_SHOW_TITLE);
            mActionBar.show();
        }
    }

    public void getView() {
        this.mAlarmBackLayout = (LinearLayout) findViewById(R.id.llAlarmBackLayout);
        this.mEnableAlarm = (TextView) findViewById(R.id.tvEnableAlarm);
        this.mSpAlarmDate = (Spinner) findViewById(R.id.spAlarmDate);
        this.mSpAlarmTime = (Spinner) findViewById(R.id.spAlarmTime);
        mTvDateTimeCurrent = (TextView) findViewById(R.id.tvDateTimeCurrent);
        mArrayDateAlarm = new ArrayAdapter<String>(NewNoteActivity.this, android.R.layout.simple_spinner_dropdown_item, mDateName);
        mArrayTimeAlarm = new ArrayAdapter<String>(NewNoteActivity.this, android.R.layout.simple_spinner_dropdown_item, mTimeName);
        mSpAlarmDate.setAdapter(mArrayDateAlarm);
        mSpAlarmTime.setAdapter(mArrayTimeAlarm);
        getTimeCurrent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        menu.removeItem(R.id.item_new_note);
        return true;
    }
}
