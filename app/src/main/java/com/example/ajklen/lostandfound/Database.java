package com.example.ajklen.lostandfound;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ajklen on 2/5/15.
 */
public class Database implements OnTaskCompleted {

    final String LINK = "http://bfbetatest.site90.com/";

    private OnTaskCompleted mCallback;
    private ArrayList<ListItem> mItemList;
    private String mTab;

    public Database (OnTaskCompleted callback){
        mCallback = callback;
    }

    public Database (OnTaskCompleted listener, String tab, ArrayList<ListItem> list){
        mCallback = listener;
        mItemList = list;
        mTab = tab;
    }

    synchronized public void fetchData (double lat, double lon){

        mItemList.clear();

        /*ListItem listitem_data[] = new ListItem[]
                {
                        new ListItem("Abhishek a b c d e f", "University of Waterloo, ON, Canada", "I lost a magnet.", 400.2),
                        new ListItem("Jon Snow", "Westeros", "Where go??", 12345),
                        new ListItem("Brad Hedges", "Somewhere", "The quick brown fox jumps over the lazy dog and this is a long string test", 4200000),
                        new ListItem("Abhishek a b c d e f", "University of Waterloo, ON, Canada", "I lost a magnet.", 400.2),
                        new ListItem("Jon Snow", "Westeros", "Where go??", 12345),
                        new ListItem("Brad Hedges", "Somewhere", "The quick brown fox jumps over the lazy dog and this is a long string test", 4200000),
                        new ListItem("Abhishek a b c d e f", "University of Waterloo, ON, Canada", "I lost a magnet.", 400.2),
                        new ListItem("Jon Snow", "Westeros", "Where go??", 12345),
                        new ListItem("Brad Hedges", "Somewhere", "The quick brown fox jumps over the lazy dog and this is a long string test", 4200000),
                        new ListItem("Abhishek a b c d e f", "University of Waterloo, ON, Canada", "I lost a magnet.", 400.2),
                        new ListItem("Jon Snow", "Westeros", "Where go??", 12345),
                        new ListItem("Brad Hedges", "Somewhere", "The quick brown fox jumps over the lazy dog and this is a long string test", 4200000)

                };

        Collections.addAll(mItemList, listitem_data);*/

        String request = "retrieve_get.php?latitude=" + lat + "&longitude=" + lon;

        new DownloadTask(this).execute(LINK + request);//"retrieve_get.php?latitude=60&longitude=-8");

        mCallback.callback(mTab);

    }

    synchronized public void fetchData (double[] loc){
        if (loc != null) {
            fetchData(loc[0], loc[1]);
        } else {
            Toast.makeText(((Fragment)mCallback).getActivity(), "Current location not available", Toast.LENGTH_SHORT).show();
        }
    }

    synchronized public void sendData(String state, String item, String location, String description, String lat, String lon){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("bfbetatest.site90.com")
                .appendPath("send_get.php")
                .appendQueryParameter("user_id", "1")
                .appendQueryParameter("state", state)
                .appendQueryParameter("item_name", item)
                .appendQueryParameter("location", location)
                .appendQueryParameter("description", description)
                .appendQueryParameter("latitude", lat)
                .appendQueryParameter("longitude", lon);
        Uri uri = builder.build();

        Log.i("Send Data", uri.toString());
        new DownloadTask(this).execute(uri.toString());
    }

    @Override
    synchronized public void callback(String result) {
        if (result.equals(DownloadTask.ERROR)) {
            Toast.makeText(((Fragment)mCallback).getActivity(), "Error connecting to server", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mItemList == null){
            mCallback.callback(null);
            return;
        }

        int n = result.indexOf("\n");

        if (n > 0 ) {
            result = result.substring(0, n);
        }

        if (mCallback!=null && mItemList!=null){
            mItemList.clear();
            for (String s : result.split("<br/>")){
                if (s.length()==0) continue;
                Log.d("database receive", s);
                if (s.split("~").length == 5) {
                    mItemList.add(new ListItem(s));
                }
            }

            mCallback.callback(null);
        }

    }
}
