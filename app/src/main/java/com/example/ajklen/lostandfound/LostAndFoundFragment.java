package com.example.ajklen.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ajklen on 2/3/15.
 */
public class LostAndFoundFragment extends Fragment implements OnTaskCompleted {

    public static String KEY_TAB = "tab_name";
    public static String LOST = "tab_lost";
    public static String FOUND = "tab_found";

    public static final String LAT = "latitude";
    public static final String LON = "longitude";
    public static final String IMG = "image";
    public static final String ITEM = "Item name";
    public static final String DESCRIPTION = "Item description";
    public static final String LOCATION = "Item location";

    private String mCurrentTab;
    private ListItemAdapter adapter;
    private ArrayList<ListItem> mItemList;
    private Database mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_lost_and_found, container, false);

        mCurrentTab = getArguments().getString(KEY_TAB);

        final ListView listview = (ListView) rootView.findViewById(R.id.lnf_list);

        /*ListItem listitem_data[] = new ListItem[]
                {
                        new ListItem("Abhishek a b c d e f", "University of Waterloo, ON, Canada", "I lost a magnet.", 400.2),
                        new ListItem("Jon Snow", "Westeros", "Where go??", 12345),
                        new ListItem("Brad Hedges", "Somewhere", "The quick brown fox jumps over the lazy dog and this is a long string test", 4200000)

                };


        mItemList = new ArrayList<>();
        for (int i = 0; i < listitem_data.length; ++i) {
            mItemList.add(listitem_data[i]);
        }*/

        mItemList = new ArrayList<>();

        adapter = new ListItemAdapter(getActivity(),
                R.layout.list_item, mItemList);

        /*adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, mItemList); */

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                final ListItem item = (ListItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(ITEM, item.item)
                        .putExtra(DESCRIPTION, item.description)
                        .putExtra(LOCATION, item.location)
                        .putExtra(LAT, item.latitude)
                        .putExtra(LON, item.longitude);

                startActivity(intent);

            }
        });

        mDatabase = new Database(this, mCurrentTab, mItemList);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabase.fetchData(((LostAndFoundActivity)getActivity()).getLocation());
            }
        }, 50);


        return rootView;
    }

    public void populateList(){

        mDatabase.fetchData(((LostAndFoundActivity) getActivity()).getLocation());

    }

    @Override
    public void callback(String result) {
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
