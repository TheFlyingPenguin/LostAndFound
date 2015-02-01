/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ajklen.lostandfound;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

/**
 * Simple Fragment used to display some meaningful content for each page in the sample's
 * {@link android.support.v4.view.ViewPager}.
 */
public class ContentFragment extends Fragment implements OnTaskCompleted{

    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";

    private LinearLayout mLayout;
    private Context context;

    /**
     * @return a new instance of {@link ContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public static ContentFragment newInstance(CharSequence title, int indicatorColor,
                                              int dividerColor) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);

        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {
            final String LINK = "http://138.51.236.172/project-118/";
            if (args.get(KEY_TITLE).equals("Lost")) {
                new DownloadTask(this).execute(LINK+"lost_items.php?user_id=1");
            }

            /*TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText("Title: " + args.getCharSequence(KEY_TITLE));

            int indicatorColor = args.getInt(KEY_INDICATOR_COLOR);
            TextView indicatorColorView = (TextView) view.findViewById(R.id.item_indicator_color);
            indicatorColorView.setText("Indicator: #" + Integer.toHexString(indicatorColor));
            indicatorColorView.setTextColor(indicatorColor);

            int dividerColor = args.getInt(KEY_DIVIDER_COLOR);
            TextView dividerColorView = (TextView) view.findViewById(R.id.item_divider_color);
            dividerColorView.setText("Divider: #" + Integer.toHexString(dividerColor));
            dividerColorView.setTextColor(dividerColor);*/
        }
    }

    @Override
    public void callback(String result) {
        List<Node> nodes = jsonToNodes(result.trim());

    }

    private class Node{
        String place, item, description;
        double lat, lon;
        boolean isLost;

        public Node(String place, String item, String description, double lat, double lon, boolean isLost) {
            this.place = place;
            this.item = item;
            this.description = description;
            this.lat = lat;
            this.lon = lon;
            this.isLost = isLost;
        }
    }

    private List<Node> jsonToNodes (String s){

        return null;
    }
}
