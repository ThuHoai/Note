package com.hoaiutc95.note.custom;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by Thu Hoai on 8/4/2017.
 */

public class CustomAdapterGridview extends ArrayAdapter {
    public CustomAdapterGridview(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}
