package com.example.ajklen.lostandfound;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateActivity extends ActionBarActivity implements OnTaskCompleted {

    public static final String MAP_NAME = "map_location";
    public static final String MAP_LAT = "map_latitude";
    public static final String MAP_LON = "map_longitude";

    private static final int ACTIVITY_MAP = 1;
    private static final int ACTIVITY_GALLERY = 2;
    private static final int ACTIVITY_CAMERA = 3;

    private Context context;
    private double currentLat;
    private double currentLon;
    private TextView locView, latView, lonView;
    private EditText itemText, descrText;
    private ImageView imgView;
    private String state;

    private byte[] img;

    private Database mDatabase;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        currentLat = getIntent().getDoubleExtra("Lat", 0);
        currentLon = getIntent().getDoubleExtra("Lon", 0);

        latView = (TextView)findViewById(R.id.text_lat);
        lonView = (TextView)findViewById(R.id.text_lon);
        locView = (TextView)findViewById(R.id.text_name);

        itemText = (EditText)findViewById(R.id.text_item);
        descrText = (EditText)findViewById(R.id.text_description);

        imgView = (ImageView)findViewById(R.id.img_btn);

        state = "lost";

        ((Switch)findViewById(R.id.switch_state)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                state = isChecked ? "found" : "lost";
            }
        });

        mDatabase = new Database(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putCharSequence(LostAndFoundFragment.LAT, latView.getText());
        outState.putCharSequence(LostAndFoundFragment.LON, lonView.getText());
        outState.putCharSequence(LostAndFoundFragment.LOCATION, locView.getText());
        outState.putByteArray(LostAndFoundFragment.IMG, img);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence s = savedInstanceState.getCharSequence(LostAndFoundFragment.LAT);
        if (s!= null) latView.setText(s);

        s = savedInstanceState.getCharSequence(LostAndFoundFragment.LON);
        if (s!= null) lonView.setText(s);

        s = savedInstanceState.getCharSequence(LostAndFoundFragment.LOCATION);
        if (s!= null) locView.setText(s);

        img = savedInstanceState.getByteArray(LostAndFoundFragment.IMG);
        if (img != null) imgView.setImageBitmap(decodeSampledBitmapFromByte(img, 200, 200));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_create, menu);
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
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(currentLat, currentLon, 1);
                if(listAddresses!=null && listAddresses.size()>0){
                    Address address = listAddresses.get(0);
                    setAddress(address);
                } else {
                    locView.setText("Coordinates:");
                }
            } catch (Exception e) {
                e.printStackTrace();
                locView.setText("Coordinates:");
            }

            latView.setText(String.valueOf(currentLat));
            lonView.setText(String.valueOf(currentLon));
        }
    }

    public void setAddress(Address address){
        locView.setText("");
        String[] fields = new String[] {address.getFeatureName(), address.getAddressLine(0),
                address.getAdminArea()};

        for (String s : fields) {
            if (s != null) locView.append(s + ", ");
        }

        locView.append(address.getCountryName());


    }

    public void setMap(View v){
        /*Uri gmUri;

        if (currentLat != 0 || currentLon != 0) {
            gmUri = Uri.parse("geo:"+currentLat+","+currentLon)
                    .buildUpon().appendQueryParameter("z", "15").build();
        }else {
            Log.e("getMap", "Location not found.");
            gmUri = Uri.parse("geo:0,0");
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, gmUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent); */


        Intent intent = new Intent(context, MapActivity.class);
        if (currentLat != 0 || currentLon != 0) {
            intent.putExtra(LostAndFoundFragment.LAT, currentLat);
            intent.putExtra(LostAndFoundFragment.LON, currentLon);
        }else {
            Log.e("getMap", "Location not found.");
        }

        startActivityForResult(intent, ACTIVITY_MAP);
    }

    public void chooseImage(View v){
        CharSequence choices[] = new CharSequence[] {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a picture");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageIntent(which);
            }
        });
        builder.show();

        /*Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, ACTIVITY_PICTURE);*/
    }

    public void onSend(View v){
        if (itemText.getText().length() == 0 || locView.getText().length() == 0){
            Toast.makeText(this, "Please enter an item name and location", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabase.sendData(state, itemText.getText().toString(), locView.getText().toString(),
                descrText.getText().toString(), latView.getText().toString(), lonView.getText().toString());
    }

    private void imageIntent(int n){
        if (n==0){
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePicture.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePicture, ACTIVITY_CAMERA);
            } else {
                Toast.makeText(this, "Error: no camera application found.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, ACTIVITY_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_MAP && resultCode==RESULT_OK) {
            locView.setText(data.getStringExtra(MAP_NAME));
            latView.setText(String.valueOf(data.getDoubleExtra(MAP_LAT, 0)));
            lonView.setText(String.valueOf(data.getDoubleExtra(MAP_LON, 0)));

        }else if (requestCode == ACTIVITY_GALLERY && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            try {
                BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(imgUri))
                        .compress(Bitmap.CompressFormat.JPEG, 100, stream);

                img = stream.toByteArray();
                imgView.setImageBitmap(decodeSampledBitmapFromByte(img, 200, 200));
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error: image not found", Toast.LENGTH_SHORT).show();
            }


        }else if (requestCode == ACTIVITY_CAMERA && resultCode==RESULT_OK){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ((Bitmap) data.getExtras().get("data")).compress(Bitmap.CompressFormat.JPEG, 100, stream);

            img = stream.toByteArray();
            imgView.setImageBitmap(decodeSampledBitmapFromByte(img ,200,200));
        }
    }

    public Bitmap decodeSampledBitmapFromByte(byte[] res,
                                              int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(res, 0, res.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(res, 0, res.length,options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

    @Override
    public void callback(String result) {
        onBackPressed();
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

    public void hideKeyboard (View v){
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
    }
}
