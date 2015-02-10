package com.example.ajklen.lostandfound;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;


public class DetailActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            DetailFragment frag = new DetailFragment();
            frag.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements OnMapReadyCallback {

        private FragmentActivity context;
        private LatLng currentPosition;

        public DetailFragment() {
        }

        @Override
        public void onAttach(Activity activity) {
            context=(FragmentActivity) activity;
            super.onAttach(activity);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Bundle args = getArguments();

            if (args != null) {
                TextView[] tv = new TextView[]{
                        (TextView) getActivity().findViewById(R.id.text_item),
                        (TextView) getActivity().findViewById(R.id.text_description),
                        (TextView) getActivity().findViewById(R.id.text_location) };

                String[] fields = new String[]{
                        args.getString(LostAndFoundFragment.ITEM),
                        args.getString(LostAndFoundFragment.DESCRIPTION),
                        args.getString(LostAndFoundFragment.LOCATION) };

                for (int i = 0; i < fields.length; i++) {
                    if (fields[i] != null) tv[i].setText(fields[i]);
                    else Log.d("detail activity", "Field " + i + " is null.");
                }

                ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.detail_map)).getMapAsync(this);
                currentPosition = new LatLng(args.getDouble(LostAndFoundFragment.LAT), args.getDouble(LostAndFoundFragment.LON));


            } else {
                Log.e(this.toString(), "No arguments given.");
            }
        }

        @Override
        public void onMapReady(GoogleMap map) {
            if (currentPosition != null){
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
                map.addMarker(new MarkerOptions().position(currentPosition));
                map.getUiSettings().setAllGesturesEnabled(false);
            }
        }
    }
}
