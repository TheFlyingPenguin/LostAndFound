package com.example.ajklen.lostandfound;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ajklen on 2/3/15.
 */
public class LostAndFoundFragment extends Fragment implements OnTaskCompleted {

    public static String KEY_TAB = "tab_name";
    public static String LOST = "tab_lost";
    public static String FOUND = "tab_found";

    private String mCurrentTab;
    private ListItemAdapter adapter;
    private ArrayList<ListItem> mItemList;
    private Database mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_lost_and_found, container, false);

        mCurrentTab = getArguments().getString(KEY_TAB);

        final ListView listview = (ListView) rootView.findViewById(R.id.lnf_list);

        ListItem listitem_data[] = new ListItem[]
                {
                        new ListItem(R.drawable.ic_launcher, "Cloudy"),
                        new ListItem(R.drawable.ic_launcher, "Showers"),
                        new ListItem(R.drawable.ic_launcher, "Snow"),
                        new ListItem(R.drawable.ic_launcher, "Storm"),
                        new ListItem(R.drawable.ic_launcher, "Sunny")
                };

        adapter = new ListItemAdapter(getActivity(),
                R.layout.list_item, listitem_data);

        mItemList = new ArrayList<ListItem>();
        for (int i = 0; i < listitem_data.length; ++i) {
            mItemList.add(listitem_data[i]);
        }

        /*adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, mItemList); */

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                final ListItem item = (ListItem) parent.getItemAtPosition(position);
               Log.d("OnClickListener", "item found at " + position);
            }
        });

        mDatabase = new Database(this, mCurrentTab, mItemList);

        return rootView;
    }

    public void populateList(){
        Log.d("populateList", "Tab: " + mCurrentTab);

        mDatabase.fetchData();

    }

    @Override
    public void callback(String result) {
        Log.d("LostAndFoundFragment callback", "the result for tab " + result);
        Log.d("LostAndFoundFragment callback", "list is now " + mItemList);

        //final ListView listview = (ListView) getActivity().findViewById(R.id.lnf_list);

        adapter.resetData(mItemList);
        adapter.notifyDataSetChanged();
    }
/*
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        public void resetData(List<String> objects){
            mIdMap.clear();
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
    */
}
