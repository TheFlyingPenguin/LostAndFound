package com.example.ajklen.lostandfound;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateActivity extends ActionBarActivity {

    public static final String LAT = "lt";
    public static final String LON = "ln";

    public static final String MAP_NAME = "Res";
    public static final String MAP_LAT = "Lat";
    public static final String MAP_LON = "Lon";

    private static final int ACTIVITY_MAP = 1;
    private static final int ACTIVITY_PICTURE= 2;

    Context context;
    private double currentLat;
    private double currentLon;
    private TextView locView, latView, lonView;
    private EditText objText, descrText;
    private ImageView imgView;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        context = CreateActivity.this;

        currentLat = getIntent().getDoubleExtra(LAT, 0);
        currentLon = getIntent().getDoubleExtra(LON, 0);

        latView = (TextView)findViewById(R.id.text_lat);
        lonView = (TextView)findViewById(R.id.text_lon);
        locView = (TextView)findViewById(R.id.text_name);

        objText = (EditText)findViewById(R.id.text_item);
        descrText = (EditText)findViewById(R.id.text_description);

        imgView = (ImageView)findViewById(R.id.img_btn);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
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

    public void setCoord(View v){
        if (currentLat != 0 || currentLon != 0) {
            latView.setText(String.valueOf(currentLat));
            lonView.setText(String.valueOf(currentLon));
        }
    }

    public void setMap(View v){
        Intent intent = new Intent(context, MapActivity.class);
        if (currentLat != 0 || currentLon != 0) {
            intent.putExtra(LAT, currentLat);
            intent.putExtra(LON, currentLon);
        }else {
            Log.e("getMap", "Location not found.");
        }

        startActivityForResult(intent, ACTIVITY_MAP);
    }

    public void chooseImage(View v){
        /*CharSequence colors[] = new CharSequence[] {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageIntent(which);
            }
        });
        builder.show();*/

        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, ACTIVITY_PICTURE);
    }

    /*private void imageIntent(int n){
        if (n==0){
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, ACTIVITY_PICTURE);
        }else{
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, ACTIVITY_PICTURE);
        }
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTIVITY_MAP && resultCode==RESULT_OK) {
            locView.setText(data.getStringExtra(MAP_NAME));
            latView.setText(String.valueOf(data.getDoubleExtra(MAP_LAT, 0)));
            lonView.setText(String.valueOf(data.getDoubleExtra(MAP_LON, 0)));

        }else if (requestCode == ACTIVITY_PICTURE && resultCode==RESULT_OK){
            imgUri = data.getData();
            imgView.setImageURI(imgUri);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {



        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create, container, false);



            Log.d("placeholder fragment", "end of oncreateview");
            return rootView;
        }


    }*/
}
