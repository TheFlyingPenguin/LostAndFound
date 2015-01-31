package com.example.ajklen.lostandfound;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Rachel on 2015-01-31.
 */
public class WebInterface {
    private Context context;
    private URI website;
    private HttpClient httpClient;
    public WebInterface (Context context){
        this.context = context;
        final String uri = "http://138.51.236.172/project-118/login.php?username=alex&password=jin";
        try {
            httpClient = new DefaultHttpClient();
            URI website = new URI(uri);
        }catch (URISyntaxException e){
            Toast.makeText(this.context, "Failed to connect to server!", Toast.LENGTH_SHORT).show();
            Log.e("Web Interface", "Server connection error: " + e.toString());
        }
    }
    public void makeRequest(){
        HttpGet request;
        try {
            request = new HttpGet();
            request.setURI(website);
            HttpResponse response = httpClient.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line = in.readLine();
            Log.d("Web Interface", "First line: "+line);
        }catch (Exception e){
            Log.e("Web Interface", "Error getting data: "+e.toString());
        }
    }
}
