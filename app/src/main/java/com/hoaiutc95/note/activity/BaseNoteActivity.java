package com.hoaiutc95.note.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hoaiutc95.note.R;
import com.hoaiutc95.note.custom.PictureItemAdapter;
import com.hoaiutc95.note.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.getInstance;

/**
 * Created by Thu Hoai on 8/6/2017.
 */

public abstract class BaseNoteActivity extends AppCompatActivity {
    protected static final int REQUEST_TAKE_PHOTO = 1;
    protected static final int REQUEST_CHOOSE_PHOTO = 2;
    protected ActionBar mActionBar;
    protected ScrollView mLayout;
    protected TextView mEnableAlarm, mTvDateTimeCurrent;
    protected LinearLayout mAlarmBackLayout;
    protected Spinner mSpAlarmDate, mSpAlarmTime;
    protected final Calendar mCalendar = getInstance();
    protected Dialog mInsertPictureDialog, mChooseColorDialog;
    protected ImageView mPicture;
    protected ArrayList<String> mPictrureList;
    protected PictureItemAdapter mPictureItemAdapter;
    protected GridView mGvPicture;
    protected Date mAlarm;
    protected ArrayAdapter<String> mArrayDateAlarm, mArrayTimeAlarm;
    protected String[] mDateName = {"Today", "Tomorrow", "Next Thursday", "Other..."};
    protected String[] mTimeName = {"9:00", "13:00", "17:00", "20:00", "Other..."};
    protected String mCurrentPhotoPath = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        setContentView(layoutId);
        mGvPicture = (GridView) findViewById(R.id.grvPicture);
        mPictrureList = new ArrayList<>();
        mPictureItemAdapter = new PictureItemAdapter(this, R.layout.gridview_item_picture, mPictrureList);
        mGvPicture.setAdapter(mPictureItemAdapter);
    }

    public abstract int getLayoutId();

    private void showInsertPictureDiaglog() {
        mInsertPictureDialog = new Dialog(this);
        mInsertPictureDialog.setContentView(R.layout.insert_picture_dialog);
        mInsertPictureDialog.setTitle("Select Picture");
        mInsertPictureDialog.show();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void takePhoto(View v) {
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
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                List<ResolveInfo> resInfoList = getApplicationContext().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getApplicationContext().grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }
        }
        mInsertPictureDialog.cancel();
    }

    public void choosePhoto(View v) {
        mInsertPictureDialog.dismiss();
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri currImageUri;
        mGvPicture.setVisibility(View.VISIBLE);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    mPictrureList.add(mCurrentPhotoPath);
                    mPictureItemAdapter.notifyDataSetChanged();
                }
                break;
                case REQUEST_CHOOSE_PHOTO: {
                    currImageUri = data.getData();
                    String path = getRealPathFromURI(currImageUri);
                    mPictrureList.add(path);
                    mPictureItemAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    public void chooseColor() {
        mChooseColorDialog = new Dialog(this);
        mChooseColorDialog.setContentView(R.layout.dialog_color);
        mChooseColorDialog.setTitle("Choose Color");
        mChooseColorDialog.show();
    }

    public void getColorClick(View v) {
        mChooseColorDialog.dismiss();
        String color = v.getTag().toString();
        mLayout = (ScrollView) findViewById(R.id.llNewNote);
        mLayout.setBackgroundColor(Color.parseColor(color));
    }

    public void getTimeCurrent() throws ParseException {
        String s = Utils.normalDateFormat.format(mCalendar.getTime());
        mTvDateTimeCurrent.setText(s);
        mAlarm = Utils.dateFormat.parse(s);
    }

    private class OnSelectedDateAlarm implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getSelectedItemPosition()) {
                case 0:
                    break;
                case 1:
                    mAlarm.setDate(mCalendar.get(Calendar.DAY_OF_YEAR) + 1);
                    break;
                case 2:
                    break;
                case 3:
                    showDatePickerDialog();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class OnSelectedTimeAlarm implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getSelectedItemPosition() == 4) {
                showTimePickerDialog();
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
                mDateName[3] = Utils.dateFormat.format(mCalendar.getTime());
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
                mTimeName[4] = Utils.hourFormat.format(mCalendar.getTime());
                mArrayTimeAlarm.notifyDataSetChanged();
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
        chooseTimeDialog.setTitle("Choose Time");
        chooseTimeDialog.show();
    }


    public void enableAlarm(View v) {
        mEnableAlarm.setVisibility(View.GONE);
        mAlarmBackLayout.setVisibility(View.VISIBLE);
        mSpAlarmDate.setOnItemSelectedListener(new OnSelectedDateAlarm());
        mSpAlarmTime.setOnItemSelectedListener(new OnSelectedTimeAlarm());
    }

    public void disableAlarm(View v) {
        mAlarmBackLayout.setVisibility(View.GONE);
        mEnableAlarm.setVisibility(View.VISIBLE);
    }

    public abstract void customActionBar();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_action_camera:
                showInsertPictureDiaglog();
                return true;
            case R.id.item_choose_color:
                chooseColor();
                return true;
            case R.id.item_save_note:
                return true;
            case R.id.item_new_note:
                return true;
            default:
                return false;
        }
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

}
