package com.hoaiutc95.note.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hoaiutc95.note.service.AlarmService;

/**
 * Created by Thu Hoai on 8/14/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

       Intent myIntent = new Intent(context, AlarmService.class);
        myIntent.putExtra("note", intent.getExtras().getSerializable("note"));
        context.startService(myIntent);
    }
}
