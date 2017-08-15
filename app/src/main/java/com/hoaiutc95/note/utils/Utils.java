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

    public static Bitmap getResizedBitmap(String filepath, int reqWidth, int reqHeight) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(filepath, options);
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
