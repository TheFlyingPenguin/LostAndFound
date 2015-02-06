package com.example.ajklen.lostandfound;

import android.app.Activity;
import android.content.Context;
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

    Context context;
    int layoutResourceId;
    ListItem data[] = null;

    public ListItemAdapter(Context context, int layoutResourceId, ListItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public void resetData(ArrayList<ListItem> list){
        data = new ListItem[list.size()];
        for (int i=0; i<list.size(); i++){
            data[i] = list.get(i);
        }
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
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (ListItemHolder)row.getTag();
        }

        ListItem ListItem = data[position];
        holder.txtTitle.setText(ListItem.title);
        holder.imgIcon.setImageResource(ListItem.icon);

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
        ImageView imgIcon;
        TextView txtTitle;
    }
}
