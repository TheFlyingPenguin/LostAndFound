package com.example.ajklen.lostandfound;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ajklen on 2/3/15.
 */
public class LostAndFoundFragment extends Fragment {

    String mText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_lost_and_found, container, false);
        String s = getArguments().getString("key");
        if (s != null){
            ((TextView)rootView.findViewById(R.id.section_label)).setText(s);
        }
        return rootView;
    }
}
