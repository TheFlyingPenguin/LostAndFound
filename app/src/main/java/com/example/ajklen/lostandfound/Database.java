package com.example.ajklen.lostandfound;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ajklen on 2/5/15.
 */
public class Database implements OnTaskCompleted {

    final String LINK = "http://138.51.236.172/project-118/";

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

        if (mTab.equals(LostAndFoundFragment.FOUND)) Log.d("Database", "found list: " + mItemList);
        mCallback.callback(mTab);

    }

    @Override
    synchronized public void callback(String result) {
        if (mCallback!=null && mItemList!=null){
            mItemList.clear();
           // mItemList.addAll(Arrays.asList(result.split("<//b>")));

            mCallback.callback(null);
        }


    }
}
