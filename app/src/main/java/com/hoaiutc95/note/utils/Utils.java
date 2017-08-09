package com.hoaiutc95.note.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thu Hoai on 8/5/2017.
 */

public class Utils {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static SimpleDateFormat normalDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM HH:mm");

}
