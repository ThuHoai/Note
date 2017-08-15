package com.hoaiutc95.note.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;


public class Note implements Serializable {
    private long mId;
    private String mTitle;
    private String mDescription;
    private String mColor;
    private String mAlarm;
    private String mCreateDate;
    private String mImagePath;

    public Note() {
    }

    public Note(long mId, String mTitle, String mDescription, String mColor, String mAlarm, String mCreateDate, String mImagePath) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        if(mColor == null){
            mColor = "#FFFFFF";
        }
        this.mColor = mColor;
        this.mAlarm = mAlarm;
        this.mCreateDate = mCreateDate;
        if (mImagePath == null || mImagePath.equals(" ")){
            mImagePath = " ";
        }
        this.mImagePath = mImagePath;
    }

    public Note(String mTitle, String mDescription, String mColor, String mAlarm, String mCreateDate, String mImagePath) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        if(mColor == null){
            mColor = "#FFFFFF";
        }
        this.mColor = mColor;
        this.mAlarm = mAlarm;
        this.mCreateDate = mCreateDate;

        if (mImagePath == null || mImagePath.equals(" ")){
            mImagePath = " ";
        }
        this.mImagePath = mImagePath;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public String getmAlarm() {
        return mAlarm;
    }

    public void setmAlarm(String mAlarm) {
        this.mAlarm = mAlarm;
    }

    public String getmCreateDate() {
        return mCreateDate;
    }

    public void setmCreateDate(String mCreateDate) {
        this.mCreateDate = mCreateDate;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }
}
