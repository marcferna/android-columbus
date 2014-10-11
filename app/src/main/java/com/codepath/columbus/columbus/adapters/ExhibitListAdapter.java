package com.codepath.columbus.columbus.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.com.codepath.columbus.columbus.models.Exhibit;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ExhibitListAdapter extends ArrayAdapter<Exhibit> {
    private static class ViewHolder {
        TextView tvExhibitTitle;
        TextView tvExhibitShortDesc;
        ImageView ivExhibitImage;
    }

    public ExhibitListAdapter(Context context, List<Exhibit> exhibits) {
        super(context, R.layout.item_exhibit_list, exhibits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Exhibit exhibit = Exhibit.testInit();
        ViewHolder viewHolder;

        // Check if this is recycled view, if not, create / inflate it.
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_exhibit_list, parent, false);
            // Get resources from view to populate
            viewHolder.tvExhibitTitle = (TextView) convertView.findViewById(R.id.tvExhibitTitle);
            viewHolder.tvExhibitShortDesc = (TextView) convertView.findViewById(R.id.tvExhibitShortDesc);
            viewHolder.ivExhibitImage = (ImageView) convertView.findViewById(R.id.ivExhibitImage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Clear recycled view
        viewHolder.ivExhibitImage.setImageResource(0);

        // Populate resources
        Log.i("INFO", "setting up data");
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(exhibit.getImageUrls().get(0), viewHolder.ivExhibitImage);
        viewHolder.tvExhibitTitle.setText(exhibit.getName());
        viewHolder.tvExhibitShortDesc.setText(exhibit.getShortDescription());

        return convertView;
    }
}
