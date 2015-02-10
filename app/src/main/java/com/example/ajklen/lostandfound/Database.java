package com.example.ajklen.lostandfound;

import android.app.Activity;
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

    @Override
    synchronized public void callback(String result) {
        if (result.equals(DownloadTask.ERROR)) {
            Toast.makeText(((Fragment)mCallback).getActivity(), "Error connecting to server", Toast.LENGTH_SHORT).show();
            return;
        }

        result = result.substring(0, result.indexOf("\n"));
        if (mCallback!=null && mItemList!=null){
            mItemList.clear();
            for (String s : result.split("<br>")){
                if (s.length()==0) continue;
                Log.d("database callback", s);
                mItemList.add(new ListItem(s));
            }

            mCallback.callback(null);
        }

    }
}
