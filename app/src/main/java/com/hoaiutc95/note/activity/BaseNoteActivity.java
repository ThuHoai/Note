package com.hoaiutc95.note.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.hoaiutc95.note.R;
import com.hoaiutc95.note.custom.PictureItemAdapter;
import com.hoaiutc95.note.database.DAOdb;
import com.hoaiutc95.note.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.getInstance;


public abstract class BaseNoteActivity extends AppCompatActivity {
    protected static final int REQUEST_TAKE_PHOTO = 1;
    protected DAOdb database;
    public static final int MULTIPLE_PERMISSIONS = 10;
    protected static final int REQUEST_CHOOSE_PHOTO = 2;
    protected EditText mTitle, mContent;
    protected TextView mCurrentDate;
    protected ActionBar mActionBar;
    protected ScrollView mLayout;
    protected LinearLayout mAlarmBackLayout;
    protected GridView mGvPicture;
    protected TextView mEnableAlarm, mTvDateTimeCurrent = null;
    protected Spinner mSpAlarmDate, mSpAlarmTime;
    protected Dialog mInsertPictureDialog, mChooseColorDialog;
    protected ArrayList<String> mPictrurePathList;
    protected PictureItemAdapter mPictureItemAdapter;
    protected ArrayAdapter<String> mArrayDateAlarm, mArrayTimeAlarm;
    protected String mCurrentPhotoPath = null;
    protected String mColor = null;
    protected int mPrevPosDateSpinner;
    protected int mPrevPostTimeSpinner;
    protected Calendar mCalendar = getInstance();
    protected static String mDate = "", mTime = "";
    protected AlarmManager alarmManager;
    protected String[] mDateName = {"Today", "Tomorrow", "Next Thursday", "Other..."};
    protected String[] mTimeName = {"9:00", "13:00", "17:00", "20:00", "Other..."};
    AdapterView.OnItemSelectedListener onDateSpinnerItemSelectedListener = new OnSelectedDateAlarm();
    AdapterView.OnItemSelectedListener onTimeSpinnerItemSelectedListener = new OnSelectedTimeAlarm();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        setContentView(layoutId);
        database = new DAOdb(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        this.mGvPicture = (GridView) findViewById(R.id.grvPicture);
        this.mPictrurePathList = new ArrayList<>();
        this.mPictureItemAdapter = new PictureItemAdapter(this, R.layout.gridview_item_picture, mPictrurePathList);
        this.mGvPicture.setAdapter(mPictureItemAdapter);
    }

    public abstract int getLayoutId();

    public abstract void saveNote();

    public class MyTextWatcher implements TextWatcher {
        public MyTextWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int i = 15;
            String title = mTitle.getText().toString().trim();
            if (title.equals("")) {
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                return;
            }
            if (title.length() <= 15) {
                i = title.length();
            }
            getSupportActionBar().setTitle(title.substring(0, i));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void customActionBar() {
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_action_previous_item);
        }
    }

    public void getTimeCurrent() {
        String s = Utils.normalDateFormat.format(mCalendar.getTime());
        mTvDateTimeCurrent.setText(s);
    }

    private void showInsertPictureDiaglog() {
        mInsertPictureDialog = new Dialog(this, R.style.Dialog);
        mInsertPictureDialog.setContentView(R.layout.insert_picture_dialog);
        mInsertPictureDialog.setTitle("Select Picture");
        mInsertPictureDialog.show();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String convertPictureListtoString() {
        String s = new String();
        for (int i = 0; i < mPictrurePathList.size(); i++) {
            s += mPictrurePathList.get(i) + ",";
        }
        Log.e("s", s.toString());
        return s;
    }

    public void takePhoto(View v) {
        if (shouldAskPermission()) {
            checkPermisstion();
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplication(), "com.hoaiutc95.note.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                List<ResolveInfo> resInfoList = getApplicationContext().getPackageManager().
                queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getApplicationContext().grantUriPermission(packageName, photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
            mInsertPictureDialog.dismiss();
        }
    }

    public void choosePhoto(View v) {
        if (shouldAskPermission()) {
            checkPermisstion();
        }
        mInsertPictureDialog.dismiss();
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, REQUEST_CHOOSE_PHOTO);
    }

    private boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void checkPermisstion() {
        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int cameraPermisstion = ActivityCompat.checkSelfPermission(this, PERMISSIONS_STORAGE[0]);
        int readPermission = ActivityCompat.checkSelfPermission(this, PERMISSIONS_STORAGE[1]);
        int writePermission = ActivityCompat.checkSelfPermission(this, PERMISSIONS_STORAGE[2]);

        if (writePermission != PackageManager.PERMISSION_GRANTED|| readPermission != PackageManager.PERMISSION_GRANTED||
                cameraPermisstion != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_STORAGE[0])||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_STORAGE[1])||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_STORAGE[2])) {

            } else {
                ActivityCompat.requestPermissions(this,
                        PERMISSIONS_STORAGE,
                        MULTIPLE_PERMISSIONS);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGvPicture.setVisibility(View.VISIBLE);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                mPictrurePathList.add(mCurrentPhotoPath);
                mPictureItemAdapter.notifyDataSetChanged();
            }
            break;
            case REQUEST_CHOOSE_PHOTO: {
                if (data != null) {
                    Uri currImageUri = data.getData();
                    String path = getRealPathFromURI(currImageUri);
                    mPictrurePathList.add(path);
                    mPictureItemAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    public void chooseColor() {
        mChooseColorDialog = new Dialog(this, R.style.Dialog);
        mChooseColorDialog.setContentView(R.layout.dialog_color);
        mChooseColorDialog.setTitle("Choose Color");
        mChooseColorDialog.show();
    }

    public void getColorClick(View v) {
        mChooseColorDialog.dismiss();
        mColor = v.getTag().toString();
        mLayout.setBackgroundColor(Color.parseColor(mColor));
    }

    public class OnSelectedDateAlarm implements AdapterView.OnItemSelectedListener {
        public OnSelectedDateAlarm() {
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    mDate = Utils.dateFormat.format(mCalendar.getTime());
                    mCalendar = getInstance();
                    mDateName[3] = "other...";
                    mArrayDateAlarm.notifyDataSetChanged();
                    mPrevPosDateSpinner = position;
                    break;
                case 1:
                    mCalendar.add(Calendar.DAY_OF_YEAR, 1);
                    mDate = Utils.dateFormat.format(mCalendar.getTime());
                    mCalendar = getInstance();
                    mDateName[3] = "other...";
                    mArrayDateAlarm.notifyDataSetChanged();
                    mPrevPosDateSpinner = position;
                    break;
                case 2:
                    int week = mCalendar.get(Calendar.DAY_OF_WEEK);
                    if (week != mCalendar.THURSDAY) {
                        int nextday = (mCalendar.SATURDAY - week + 5) % 7;
                        mCalendar.add(week, nextday);
                        mDate = Utils.dateFormat.format(mCalendar.getTime());
                    } else {
                        mCalendar.add(week, 7);
                        mDate = Utils.dateFormat.format(mCalendar.getTime());
                    }
                    mCalendar = getInstance();
                    mPrevPosDateSpinner = position;
                    mDateName[3] = "other...";
                    mArrayDateAlarm.notifyDataSetChanged();
                    mPrevPosDateSpinner = position;
                    break;
                case 3:
                    if (position != mPrevPosDateSpinner) {
                        showDatePickerDialog();
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getApplicationContext(), "No No No!!!", Toast.LENGTH_LONG).show();
        }
    }

    public class OnSelectedTimeAlarm implements AdapterView.OnItemSelectedListener {

        public OnSelectedTimeAlarm() {
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                case 3:
                    mTime = mSpAlarmTime.getSelectedItem().toString();
                    mPrevPostTimeSpinner = position;
                    mTimeName[4] = "Other...";
                    mArrayTimeAlarm.notifyDataSetChanged();
                    break;
                case 4:
                    if (position != mPrevPostTimeSpinner) {
                        showTimePickerDialog();
                    }
                    break;
                default:
                    return;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getApplicationContext(), "No No No!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog chooseDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(year, month, dayOfMonth);
                mDate = Utils.dateFormat.format(mCalendar.getTime());
                mDateName[3] = mDate;
                mArrayDateAlarm.notifyDataSetChanged();
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(DAY_OF_MONTH));
        chooseDateDialog.setTitle("Choose Date");
        chooseDateDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog chooseTimeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                mTime = Utils.hourFormat.format(mCalendar.getTime());
                mTimeName[4] = mTime;
                mArrayTimeAlarm.notifyDataSetChanged();
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
        chooseTimeDialog.setTitle("Choose Time");
        chooseTimeDialog.show();
    }


    public void enableAlarm(View v) {
        mEnableAlarm.setVisibility(View.GONE);
        mAlarmBackLayout.setVisibility(View.VISIBLE);
        mSpAlarmDate.setOnItemSelectedListener(onDateSpinnerItemSelectedListener);
        mSpAlarmTime.setOnItemSelectedListener(onTimeSpinnerItemSelectedListener);
    }

    public void disableAlarm(View v) {
        mAlarmBackLayout.setVisibility(View.GONE);
        mEnableAlarm.setVisibility(View.VISIBLE);
        mSpAlarmTime.setOnItemSelectedListener(null);
        mSpAlarmDate.setOnItemSelectedListener(null);
        mTime = "";
        mDate = "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_action_camera:
                showInsertPictureDiaglog();
                break;
            case R.id.item_choose_color:
                chooseColor();
                break;
            case R.id.item_save_note:
                saveNote();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.item_new_note:
                startActivity(new Intent(getApplicationContext(), NewNoteActivity.class));
                break;
        }
        return true;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj,
                null,
                null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

 /*   public void addAlarm(Note note) {
        Date date = new Date();
        try {
            date = Utils.normalDateFormat.parse(note.getmAlarm());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long time = Long.valueOf(date.getTime());
        PendingIntent pendingIntent = null;
        if (time.longValue() > System.currentTimeMillis()) {
            Intent intentAlarm = new Intent(getApplicationContext(), AlarmService.class);
            intentAlarm.putExtra("note", note);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) note.getmId(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.longValue(), pendingIntent);
    }*/
}