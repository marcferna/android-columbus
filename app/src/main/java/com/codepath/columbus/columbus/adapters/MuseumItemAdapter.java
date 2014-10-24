package com.codepath.columbus.columbus.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.activities.ExhibitListActivity;
import com.codepath.columbus.columbus.models.Museum;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class MuseumItemAdapter extends ArrayAdapter<Museum> {

    private Activity activity;

    public MuseumItemAdapter(Context context, int resource, List<Museum> museums) {
        super(context, resource, museums);
        activity = (Activity)context;
    }

    public class ViewHolder{
        FrameLayout llMuseum;
        TextView tvMuseumName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Museum museum = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.museum_item,parent,false);

            viewHolder.llMuseum = (FrameLayout)convertView.findViewById(R.id.llMuseumItem);
            viewHolder.tvMuseumName = (TextView)convertView.findViewById(R.id.tvMuseum);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // setting the listener on the Museum
        viewHolder.llMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ExhibitListActivity.class);
                i.putExtra("museumId",museum.getObjectId());
                i.putExtra("museumUUID",museum.getBeaconUUID());
                i.putExtra("museumNickname",museum.getNickname());
                activity.startActivity(i);
            }
        });

        viewHolder.llMuseum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    view.setAlpha(0.8f);
                }else {
                    view.setAlpha(1f);
                }
                return false;
            }
        });

        ImageLoader.getInstance().loadImage(museum.getImageUrl(), new ImageLoadingListener() {
            public void onLoadingStarted(String imageUri, View view) {
                // show the progress bar
            }

            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // remove the progress bar
                viewHolder.llMuseum.setBackground(new BitmapDrawable(viewHolder.llMuseum.getResources(), loadedImage));
            }

            public void onLoadingCancelled(String imageUri, View view) {
            }
        });

        viewHolder.tvMuseumName.setText(museum.getName());
        return convertView;
    }
}
