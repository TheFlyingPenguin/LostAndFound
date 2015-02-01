package com.example.ajklen.lostandfound;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by ajklen on 1/31/15.
 */
public class MapActivity extends Activity {

    private EditText searchBar;
    private final int NUM_SEARCH_LETTERS = 1;
    private String lastString;
    ArrayList<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        lastString="";
        results = new ArrayList<String>();
        private final String LINK = "http://138.51.236.172/project-118/";

        searchBar = (EditText)findViewById(R.id.locationSearch);
        searchBar.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){


            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (s.length()>=NUM_SEARCH_LETTERS && !s.toString().equals(lastString)){
                    lastString = s.toString();
                    String request = LINK+"autocomplete.php?"+lastString;
                    new MainActivity.DownloadTask(this).execute(request);
                    for (int i=0; i< )

                }

            }
            public void afterTextChanged(Editable s) {

            }


        });
    }
}
