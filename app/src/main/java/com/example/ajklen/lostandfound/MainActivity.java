package com.example.ajklen.lostandfound;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int ACTIVITY_GOOGLE_PLAY = 1;
    private GoogleApiClient googleClient;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        textView = (TextView)findViewById(R.id.textView);

        googleClient =  new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleClient.connect();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTIVITY_GOOGLE_PLAY) {
            if(resultCode == RESULT_OK){
                //String result=data.getStringExtra("result");
            }

            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Please update Google Play APK.", Toast.LENGTH_SHORT).show();
                this.onDestroy();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int returnCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        if (returnCode != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(returnCode, MainActivity.this, ACTIVITY_GOOGLE_PLAY);
            if (dialog != null) dialog.show();
            //startActivityForResult(intent, ACTIVITY_GOOGLE_PLAY);

        }
    }

    public void getCoord(View v){
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleClient);
        if (lastLocation != null) {
            Log.d("latitude", String.valueOf(lastLocation.getLatitude()));
            textView.setText(String.valueOf(lastLocation.getLatitude())+"\n"+String.valueOf(lastLocation.getLongitude()));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
