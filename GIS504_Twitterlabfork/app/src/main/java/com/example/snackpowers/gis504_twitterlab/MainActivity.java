package com.example.snackpowers.gis504_twitterlab;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends Activity implements
GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
private ShareActionProvider mShareActionProvider;
    //define variables and tell it what scope they are.

    protected GoogleApiClient mGoogleApiClient;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected Location mLastLocation;
    protected String mShare;

    protected static final String TAG = "FINDING LOCATION, WAIT!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        buildGoogleApiClient();}


    public void buttonOnClick(View v){
        //Do something when the button is clicked
        Button button=(Button)  v;
        ((Button) v).setText("clicked");
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    public void onConnected(Bundle connectionHint){
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            mShare = "I'm at " + String.valueOf(mLastLocation.getLatitude()) + " degrees Latitude and " + String.valueOf(mLastLocation.getLongitude()) + " degrees Longtitude";

        }
        else{
            Toast.makeText(this, "No Location detected!", Toast.LENGTH_LONG).show();
           
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode()= " + result.getErrorCode());
    }
    @Override
    public void onConnectionSuspended(int cause){
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        //fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareActionProvider.setShareIntent(createShareIntent());

        //Return true to display menu
        return true;
    }
    //Call to update the share intent
    private void setShareIntent(Intent shareIntent){
        if (mShareActionProvider !=null){
            mShareActionProvider.setShareIntent(shareIntent);

        }
        else {
            Toast.makeText(this, "cannot access Twitter!! Boo! Try again", Toast.LENGTH_LONG).show();
        }
    }



    private Intent createShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT,mShare);
        return intent;
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
}
