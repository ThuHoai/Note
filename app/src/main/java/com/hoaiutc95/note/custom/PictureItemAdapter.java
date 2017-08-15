package com.hoaiutc95.note.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hoaiutc95.note.R;
import com.hoaiutc95.note.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PictureItemAdapter extends ArrayAdapter {
    private static final int itemWith = 150;
    private Context myContext;
    int mLayout;
    ArrayList<String> pathImageList = new ArrayList<>();
    private LruCache<String, Bitmap> mMemoryCache;

    public PictureItemAdapter(Context myContext, int mLayout, ArrayList<String> pathImageList) {
        super(myContext, mLayout, pathImageList);
        this.myContext = myContext;
        this.mLayout = mLayout;
        this.pathImageList = pathImageList;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    private class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
        public String paths;
        private final WeakReference<ViewHolder> imageViewReference;

        public BitmapWorkerTask(String paths, ViewHolder imageViewReference) {
            this.paths = paths;
            this.imageViewReference = new WeakReference<ViewHolder>(imageViewReference);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return Utils.getResizedBitmap(paths, PictureItemAdapter.itemWith, PictureItemAdapter.itemWith);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (imageViewReference != null && bitmap != null) {
                final ViewHolder view = imageViewReference.get();
                view.mIvPicture.setImageBitmap(bitmap);
            }
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(mLayout, parent, false);
            holder.mIvPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
            holder.mIBtnRemovePicture = (ImageButton) convertView.findViewById(R.id.ivRemovePicture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mIBtnRemovePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathImageList.remove(position);
                notifyDataSetChanged();
            }
        });
        Bitmap b = (Bitmap) mMemoryCache.get(pathImageList.get(position));
        if (b != null) {
            holder.mIvPicture.setImageBitmap(b);
        } else {
            new BitmapWorkerTask((String) this.pathImageList.get(position), holder).execute(new Void[0]);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageButton mIBtnRemovePicture;
        ImageView mIvPicture;
    }
}
