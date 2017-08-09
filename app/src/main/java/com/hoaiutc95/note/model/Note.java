package com.hoaiutc95.note.model;

import java.util.Date;

/**
 * Created by Thu Hoai on 8/3/2017.
 */

public class Note {
    private long mId;
    private String mTitle;
    private String mDescription;
    private String mColor;
    private Date mAlarm;
    private Date mCreateDate;
    private String mImagePath;

    public Note(long mId, String mTitle, String mDescription, String mColor, Date mAlarm, Date mCreateDate, String mImagePath) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mColor = mColor;
        this.mAlarm = mAlarm;
        this.mCreateDate = mCreateDate;
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

    public Date getmAlarm() {
        return mAlarm;
    }

    public void setmAlarm(Date mAlarm) {
        this.mAlarm = mAlarm;
    }

    public Date getmCreateDate() {
        return mCreateDate;
    }

    public void setmCreateDate(Date mCreateDate) {
        this.mCreateDate = mCreateDate;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }
}
