package com.example.ajklen.lostandfound;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ajklen on 2/5/15.
 */
public class Database implements OnTaskCompleted {

    final String LINK = "http://138.51.236.172/project-118/";

    private OnTaskCompleted mCallback;
    private ArrayList<String> mItemList;
    private String mTab;

    public Database (OnTaskCompleted listener, String tab, ArrayList list){
        mCallback = listener;
        mItemList = list;
        mTab = tab;
    }

    synchronized public void fetchData (){

        mItemList.clear();

        Log.d("fetchData", "tab is " + mTab);
        for (int i=0; i<20; i++){
            mItemList.add("Object number " + i);
        }

        if (mTab.equals(LostAndFoundFragment.FOUND)) Log.d("Database", "found list: " + mItemList);
        mCallback.callback(mTab);

    }

    @Override
    synchronized public void callback(String result) {
        if (mCallback!=null && mItemList!=null){
            mItemList.clear();
            mItemList.addAll(Arrays.asList(result.split("<//b>")));

            mCallback.callback(null);
        }


    }
}
