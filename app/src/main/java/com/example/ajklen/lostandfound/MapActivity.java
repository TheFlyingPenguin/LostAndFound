package com.example.ajklen.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ajklen on 1/31/15.
 */
public class MapActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnTaskCompleted{

    private EditText searchBar;
    private final int NUM_SEARCH_LETTERS = 1;
    final String LINK = "http://138.51.236.172/project-118/";
    private String lastString;
    List<String> resultArray;
    final OnTaskCompleted complete = this;
    private AutoCompleteTextView autocomplete;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        lastString="";
        resultArray = new ArrayList<String>();
        adapter = new ArrayAdapter(this, R.layout.activity_map, resultArray);
        autocomplete = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        autocomplete.setAdapter(adapter);


        searchBar = (EditText)findViewById(R.id.autoCompleteTextView1);
        searchBar.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){


            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (s.length()>=NUM_SEARCH_LETTERS && !s.toString().equals(lastString)){
                    lastString = s.toString();
                    String request = LINK+"places.php?user_id=1&string="+lastString.replace(" ","%20");
                    new DownloadTask(complete).execute(request);
                }

            }
            public void afterTextChanged(Editable s) {

            }


        });
    }

    public void searchLoc(View v){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void callback(String result) {
        Log.d("callback", result);
        resultArray = new ArrayList<String>(Arrays.asList(result.split("\\*")));
        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_map, R.id.autoCompleteTextView1, resultArray);
        autocomplete.setAdapter(adapter);
        Log.i("Callback", resultArray.get(0));
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            return rootView;
        }
    }
}