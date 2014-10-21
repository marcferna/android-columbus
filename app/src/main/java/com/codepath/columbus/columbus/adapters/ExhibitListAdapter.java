package com.codepath.columbus.columbus.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.activities.ExhibitActivity;
import com.codepath.columbus.columbus.models.Exhibit;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ExhibitListAdapter extends ArrayAdapter<Exhibit> implements StickyListHeadersAdapter {
    private ArrayList<Exhibit> exhibitsList;
    private Activity activity;

    private static class ViewHolder {
        TextView tvExhibitTitle;
        TextView tvExhibitShortDesc;
        TextView tvDistance;
        ImageView ivExhibitImage;
    }

    public ExhibitListAdapter(Context context, ArrayList<Exhibit> exhibits) {
        super(context, R.layout.item_exhibit_list, exhibits);
        exhibitsList = exhibits;
        activity = (Activity)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Exhibit exhibit = (Exhibit) getItem(position);
        ViewHolder viewHolder;

        // Check if this is recycled view, if not, create / inflate it.
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_exhibit_list, parent, false);
            // Get resources from view to populate
            viewHolder.tvExhibitTitle = (TextView) convertView.findViewById(R.id.tvExhibitTitle);
            viewHolder.tvExhibitShortDesc = (TextView) convertView.findViewById(R.id.tvExhibitShortDesc);
            viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            viewHolder.ivExhibitImage = (ImageView) convertView.findViewById(R.id.ivExhibitImage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Clear recycled view
        viewHolder.ivExhibitImage.setImageResource(0);

        // Populate resources
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(exhibit.getImageUrls().get(0), viewHolder.ivExhibitImage);
        viewHolder.tvExhibitTitle.setText(exhibit.getName());
        viewHolder.tvExhibitShortDesc.setText(exhibit.getDescriptionShort());
        if(exhibit.getDistance() == 0) {
            viewHolder.tvDistance.setVisibility(View.GONE);
        } else {
            viewHolder.tvDistance.setVisibility(View.VISIBLE);
            //viewHolder.tvDistance.setText(Double.toString(exhibit.getDistance()) + " m");
            viewHolder.tvDistance.setText("In close proximity");
        }

        return convertView;
    }

    class HeaderViewHolder {
        TextView text;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header_list, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.tvHeader);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text
        String headerText;
        Exhibit item = exhibitsList.get(position);
        if(item.getDistance() > 0) {
            headerText = "Nearby items";
        } else {
            headerText = "All items";
        }

        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        Exhibit item = exhibitsList.get(position);
        if(item.getDistance() > 0) {
            Log.d("DEBUG", "exhibit " + item.getName() + " distance = " + item.getDistance());
            return 1;
        }
        return 0;
    }
}