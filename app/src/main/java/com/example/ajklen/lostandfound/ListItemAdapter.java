package com.example.ajklen.lostandfound;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ajklen on 2/6/15.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ListItem> data = null;

    public ListItemAdapter(Context context, int layoutResourceId, ArrayList<ListItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListItemHolder();
            holder.txtItem = (TextView)row.findViewById(R.id.text_item);
            holder.txtLocation = (TextView)row.findViewById(R.id.text_location);
            holder.txtDescription = (TextView)row.findViewById(R.id.text_description);
            holder.txtDistance = (TextView)row.findViewById(R.id.text_distance);

            row.setTag(holder);
        }
        else
        {
            holder = (ListItemHolder)row.getTag();
        }

        ListItem listItem = data.get(position);
        holder.txtItem.setText(listItem.item);
        holder.txtLocation.setText(listItem.location);
        holder.txtDescription.setText(listItem.description);

        double d = listItem.distance;
        if (d > 1000000) {
            holder.txtDistance.setText("over 1000 km away");
        } else {
            holder.txtDistance.setText(String.format("%.2f %s away",
                    d > 1000 ? d / 1000 : d,
                    d > 1000 ? "km" : "m"));
        }



        return row;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class ListItemHolder
    {
        TextView txtItem;
        TextView txtLocation;
        TextView txtDescription;
        TextView txtDistance;
    }
}
