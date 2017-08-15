package com.hoaiutc95.note.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.hoaiutc95.note.R;
import com.hoaiutc95.note.activity.MainActivity;
import com.hoaiutc95.note.model.Note;
import com.hoaiutc95.note.utils.Utils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Thu Hoai on 8/13/2017.
 */

public class AlarmService extends Service {

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!(intent == null || intent.getExtras() == null)) {
            Note note = (Note) intent.getExtras().getSerializable("note");
            if (note != null) {
                pushNotification(note);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void pushNotification(Note note) {
        if (note != null) {
            Date date = null;
            try {
                date = Utils.normalDateFormat.parse(note.getmAlarm());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            NotificationManager notifMng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.drawable.ic_launcher, "Time", date.getTime());
            notifMng.notify((int) note.getmId(), notification);
        }
    }
}

