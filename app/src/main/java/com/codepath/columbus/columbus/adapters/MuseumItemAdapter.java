package com.codepath.columbus.columbus.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.models.Museum;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class MuseumItemAdapter extends ArrayAdapter<Museum> {

    public MuseumItemAdapter(Context context, int resource, List<Museum> museums) {
        super(context, resource, museums);
    }

    // TODO - ViewHolder
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Museum museum = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.museum_item,parent,false);
        }

        final LinearLayout llMuseum = (LinearLayout)convertView.findViewById(R.id.llMuseumItem);
        ImageLoader.getInstance().loadImage(museum.url, new ImageLoadingListener() {
            public void onLoadingStarted(String imageUri, View view) {
                // show the progress bar
            }

            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // remove the progress bar
                llMuseum.setBackground(new BitmapDrawable(llMuseum.getResources(),loadedImage));
            }

            public void onLoadingCancelled(String imageUri, View view) {
            }
        });

        TextView tvMuseumName = (TextView)convertView.findViewById(R.id.tvMuseum);
        tvMuseumName.setText(museum.name);

        return convertView;
    }
}
