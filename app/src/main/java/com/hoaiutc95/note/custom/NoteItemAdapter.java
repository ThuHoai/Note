package com.hoaiutc95.note.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoaiutc95.note.R;
import com.hoaiutc95.note.model.Note;
import com.hoaiutc95.note.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hoaiutc95.note.utils.Utils.shortDateFormat;

/**
 * Created by Thu Hoai on 8/4/2017.
 */

public class NoteItemAdapter extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<Note> mListNotes;

    public NoteItemAdapter(Context context, int layout, List<Note> noteList) {
        this.mContext = context;
        this.mLayout = layout;
        this.mListNotes = noteList;
    }


    @Override
    public int getCount() {
        return mListNotes.size();
    }

    @Override
    public Object getItem(int i) {
        return mListNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolde {
        TextView title, note, dateCurrent;
        RelativeLayout linearLayout;
        ImageView imageNote;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = view;
        ViewHolde holde = new ViewHolde();
        if (row == null) {
            row = inflater.inflate(mLayout, null);
            holde.title = (TextView) row.findViewById(R.id.item_title);
            holde.note = (TextView) row.findViewById(R.id.item_Note);
            holde.dateCurrent = (TextView) row.findViewById(R.id.item_dateCurrent);
            holde.linearLayout = (RelativeLayout) row.findViewById(R.id.item_layout);
            holde.imageNote = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holde);
        } else {
            holde = (ViewHolde) row.getTag();
        }
        Note note = mListNotes.get(i);
        holde.title.setText(note.getmTitle());
        holde.note.setText(note.getmDescription());
        holde.dateCurrent.setText(note.getmCreateDate());

        holde.linearLayout.setBackgroundColor(Color.parseColor(note.getmColor()));
        try {
            Date date = new Date();
            date = Utils.normalDateFormat.parse(note.getmCreateDate());
            holde.dateCurrent.setText(Utils.shortDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (note.getmAlarm().equals("")) {
            holde.imageNote.setVisibility(View.GONE);
        } else {
            holde.imageNote.setVisibility(View.VISIBLE);
        }
        return row;
    }
}