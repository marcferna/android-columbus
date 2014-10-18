package com.codepath.columbus.columbus.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    // TODO - ViewHolder
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Museum museum = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.museum_item,parent,false);
        }

        final LinearLayout llMuseum = (LinearLayout)convertView.findViewById(R.id.llMuseumItem);

        // setting the listener on the Museum
        llMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ExhibitListActivity.class);
                i.putExtra("museumId",museum.getObjectId());
                i.putExtra("museumUUID",museum.getBeaconUUID());
                activity.startActivity(i);
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
                llMuseum.setBackground(new BitmapDrawable(llMuseum.getResources(),loadedImage));
            }

            public void onLoadingCancelled(String imageUri, View view) {
            }
        });

        TextView tvMuseumName = (TextView)convertView.findViewById(R.id.tvMuseum);
        String name = "<b><font color=\"#15c2fb\">"+museum.getName()+" </b></font>";
        tvMuseumName.setText(Html.fromHtml(name));

        return convertView;
    }
}
