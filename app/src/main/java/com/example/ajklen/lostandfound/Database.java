package com.example.ajklen.lostandfound;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ajklen on 2/5/15.
 */
public class Database implements OnTaskCompleted {

    final String LINK = "127.0.0.1/foundit/";

    private OnTaskCompleted mCallback;
    private ArrayList<ListItem> mItemList;
    private String mTab;

    public Database (OnTaskCompleted listener, String tab, ArrayList<ListItem> list){
        mCallback = listener;
        mItemList = list;
        mTab = tab;
    }

    synchronized public void fetchData (){

        mItemList.clear();

        Log.d("fetchData", "tab is " + mTab);

        ListItem listitem_data[] = new ListItem[]
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

        Collections.addAll(mItemList, listitem_data);

        new DownloadTask(this).execute("129.97.125.235/test.php");//"retrieve_get.php?latitude=60&longitude=-8");

        mCallback.callback(mTab);

    }

    @Override
    synchronized public void callback(String result) {
        if (result.equals(DownloadTask.ERROR)) return;

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
