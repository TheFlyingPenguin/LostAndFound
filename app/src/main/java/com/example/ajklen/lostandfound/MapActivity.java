package com.example.ajklen.lostandfound;

import android.app.Activity;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ajklen on 1/31/15.
 */
public class MapActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnTaskCompleted{

    private EditText searchBar;
    private final int NUM_SEARCH_LETTERS = 1;
    final String LINK = "http://138.51.236.172/project-118/";
    private String lastString;
    ArrayList<String> resultArray;
    private int mChars = 200;
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
                    String request = LINK+"autocomplete.php?"+lastString;
                    new DownloadTask(complete).execute(request);
                }

            }
            public void afterTextChanged(Editable s) {

            }


        });
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jResults = gson.fromJson(result, JsonObject.class);
        if (!jResults.isJsonNull()){
            JsonArray predict = jResults.getAsJsonArray("predictions");
            for (JsonElement j:predict){
                resultArray.add(j.getAsJsonObject().get("description").getAsString());
            }
        }

        if (resultArray!=null){

            adapter.clear();
            for (String r : resultArray){
                adapter.insert(r, adapter.getCount());
            }
            adapter.notifyDataSetChanged();
        }

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

    public class DownloadTask extends AsyncTask<String, Void, String> {
        OnTaskCompleted listener;
        public DownloadTask(OnTaskCompleted listener){
            this.listener=listener;
        }

        protected void onPostExecute(String r){
            listener.callback(r);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return getString(R.string.connection_error);
            }
        }
    }

    /** Initiates the fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream, mChars);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
        // END_INCLUDE(get_inputstream)
    }

    /** Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from targeted site.
     * @param len Length of string that this method returns.
     * @return String concatenated according to len parameter.
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
