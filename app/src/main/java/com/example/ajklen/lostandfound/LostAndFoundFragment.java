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
    private StableArrayAdapter adapter;
    private ArrayList<String> mItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_lost_and_found, container, false);

        mCurrentTab = getArguments().getString(KEY_TAB);

        final ListView listview = (ListView) rootView.findViewById(R.id.lnf_list);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        mItemList = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            mItemList.add(values[i]);
        }

        adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, mItemList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mItemList.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }
        });

        return rootView;
    }

    public void populateList(){
        Log.d("populateList", "Tab: " + mCurrentTab);

        adapter.clear();
        mItemList.clear();
        Database.fetchData(this, mCurrentTab, mItemList);

    }

    @Override
    synchronized public void callback(String result) {
        final ListView listview = (ListView) getActivity().findViewById(R.id.lnf_list);

        adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, mItemList);
        adapter.notifyDataSetChanged();

        listview.clearDisappearingChildren();
        listview.setAdapter(adapter);

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
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
}
