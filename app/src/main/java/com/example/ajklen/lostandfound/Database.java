package com.example.ajklen.lostandfound;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ajklen on 2/5/15.
 */
public class Database implements OnTaskCompleted {

    final String LINK = "http://138.51.236.172/project-118/";

    private static OnTaskCompleted mCallback;
    private static ArrayList<String> mItemList;

    synchronized public static void fetchData (OnTaskCompleted activity, String tab, ArrayList<String> itemList){
        mCallback = activity;
        mItemList = itemList;

        mItemList.clear();

        Log.d("fetchData", "tab is " + tab);
        for (int i=0; i<20; i++){
            mItemList.add("Object number " + i);
        }

        mCallback.callback(null);

    }

    @Override
    synchronized public void callback(String result) {
        if (mCallback!=null && mItemList!=null){
            mItemList.clear();
            mItemList.addAll(Arrays.asList(result.split("<//b>")));

            mCallback.callback(result);
        }


    }
}
