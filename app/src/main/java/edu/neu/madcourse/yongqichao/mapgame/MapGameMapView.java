package edu.neu.madcourse.yongqichao.mapgame;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.yongqichao.R;


public class MapGameMapView extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMarkerClickListener,
        LocationListener,
        SensorEventListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker destMarker;

    LatLng myLatlng;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean isSensorPresent = false;
    TextView userNameMapView, levelMapView, scoreMapView;
    private DatabaseReference mDatabase;

    public String username; // the most important id in this activity
    public Integer level;
    public Integer score;
    public List<GameMarker> RedMarker = new ArrayList<>();
    public List<GameMarker> GreenMarker = new ArrayList<>();
    private Integer markerAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapgame_main);

        getSupportActionBar().hide();

        userNameMapView = (TextView) findViewById(R.id.usernameMapView);
        Bundle b = this.getIntent().getExtras();
        userNameMapView.setText("User:" + b.getString("username"));
        username = b.getString("username");
        levelMapView = (TextView) findViewById(R.id.levelMapView);
        scoreMapView = (TextView) findViewById(R.id.scoreMapView);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        isSensorPresent = true;


        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        loadUserList();

        AlertDialog.Builder builder2 = new AlertDialog.Builder(MapGameMapView.this);
        builder2.setMessage("You can also place coins for other players");
        builder2.setCancelable(false);
        builder2.setPositiveButton("Start Running",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                    }
                });
        mDialog = builder2.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(MapGameMapView.this);
        builder.setMessage("Find Golden Coins on the Map");
        builder.setCancelable(false);
        builder.setPositiveButton("Start Running",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                    }
                });
        mDialog = builder.show();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////FOLLOWING IS SAVING & LOADING USER INFO TO DATABASE/////////////////////////////

    public void loadUserList() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                markerAmount = Integer.parseInt(dataSnapshot.child("markerAmount").getValue().toString());
                System.out.println(markerAmount + "marker amount !!!!!!!!!!!");
                if (!dataSnapshot.child("Users").hasChild(username)) {
                    mDatabase.child("mapGameRecords").child("Users").child(username);
                    createNewUser();
                    level = 1;
                    score = 0;
                    levelMapView.setText("level:" + 1);
                    scoreMapView.setText("score:" + 0);
                } else {
                    GameUser User = dataSnapshot.child("Users").child(username).getValue(GameUser.class);
                    level = User.level;
                    score = User.score;
                    levelMapView.setText("level:" + level);
                    scoreMapView.setText("score:" + score);
                }
                RedMarker.clear();
                GreenMarker.clear();
                for (DataSnapshot child : dataSnapshot.child("Markers").getChildren()) {
                    GameMarker marker = child.getValue(GameMarker.class);
                    if (marker.username.equals(username)) RedMarker.add(marker);
                    else GreenMarker.add(marker);
                }
                updateMarker();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.child("mapGameRecords").addValueEventListener(postListener);
    }

    public void saveUserList() {
        GameUser newUser = new GameUser(username, level, score);
        mDatabase.child("mapGameRecords").child("Users").child(username).setValue(newUser);
    }

    private void createNewUser() {
        GameUser newUser = new GameUser();
        newUser.username = username;
        mDatabase.child("mapGameRecords").child("Users").child(username).setValue(newUser);
    }

    private void createNewMarker(Double lat, Double lng) {
        GameMarker newMarker = new GameMarker();
        newMarker.markerId = markerAmount + 1;
        newMarker.username = username;
        newMarker.latitude = lat;
        newMarker.longitude = lng;
        mDatabase.child("mapGameRecords").child("Markers").child(Integer.toString(markerAmount + 1)).setValue(newMarker);
        mDatabase.child("mapGameRecords").child("markerAmount").setValue(markerAmount + 1);
        RedMarker.add(newMarker);
    }

    ////////////////////ABOVE IS SAVING & LOADING USER INFO TO DATABASE/////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////FOLLOWING IS Game Logic/////////////////////////////////////////////////////////

    public void updateMarker() {
        mGoogleMap.clear();

        for (GameMarker m : RedMarker) {
            LatLng l = new LatLng(m.latitude, m.longitude);
            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(l).title("NotAvailable")
                    .icon(BitmapDescriptorFactory.fromResource(R.raw.redcoin)));
        }

        for (GameMarker m : GreenMarker) {
            LatLng l = new LatLng(m.latitude, m.longitude);
            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(l).title(m.markerId.toString())
                    .icon(BitmapDescriptorFactory.fromResource(R.raw.coin)));
        }
        mGoogleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        LatLng dest = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude); // markerPoints.get(1);

        // move the camera
        if ((destMarker != null && !marker.getId().equals(destMarker.getId()))
                || (destMarker == null)) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(dest)      // Sets the center of the map to Mountain View
                    .zoom(15)                   // Sets the zoom
                    .bearing(140)                // Sets the orientation of the camera to east
                    .tilt(60)      // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
        }

        //find whether you are near a coin
        if (!marker.getTitle().equals("NotAvailable")) {
            //myLatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if ((myLatlng.latitude + 0.002 > dest.latitude) && (myLatlng.latitude - 0.002 < dest.latitude)
                    && (myLatlng.longitude + 0.002 > dest.longitude) && (myLatlng.longitude - 0.002 < dest.longitude)) {

                score += 100;
                mDatabase.child("mapGameRecords").child("Users").child(username).child("score").setValue(score);

                mDatabase.child("mapGameRecords").child("Markers").child(marker.getTitle()).removeValue();

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(dest)      // Sets the center of the map to Mountain View
                        .zoom(15)                   // Sets the zoom
                        .bearing(140)                // Sets the orientation of the camera to east
                        .tilt(60)// Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);

                if (score >= level * 200) {
                    level += 1;
                    mDatabase.child("mapGameRecords").child("Users").child(username).child("level").setValue(level);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapGameMapView.this);
                    builder.setMessage("  Level UP !!");
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok_label_wordgame,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent street = new Intent(MapGameMapView.this, MapGameStreetView.class);
                                    street.putExtra("lat", myLatlng);// usernameText.getText().toString());
                                    startActivity(street);
                                }
                            });
                    mDialog = builder.show();
                } else {
                    Intent street = new Intent(MapGameMapView.this, MapGameStreetView.class);
                    street.putExtra("lat", myLatlng);// usernameText.getText().toString());
                    startActivity(street);
                }
                destMarker = null;

                return true;
            }
            //draw a direction line
            else {
                System.out.println("your level is !!!!" + score);
                LatLng origin = myLatlng;
                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
                destMarker = marker;
                updateMarker();
            }
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_map));



        updateMarker();
        //enable adding marker
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                System.out.println("d(, e.toString());");

                createNewMarker(point.latitude, point.longitude);

                if (destMarker != null) {
                    onMarkerClick(destMarker);
                } else updateMarker();
            }
        });
    }

    ////////////////////Above IS Game Logic/////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////FOLLOWING IS Calculating Direction//////////////////////////////////////////////

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        //move camera
        if (mCurrLocationMarker == null) {
            myLatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 11));
        }

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        myLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myLatlng);
        markerOptions.title("Player1 Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.raw.pegman));
        markerOptions.alpha(0.8f);
        markerOptions.snippet("you are player");
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        System.out.println("your score is !!!!" + score);

        float[] result = new float[1]; // save distance
        if (destMarker != null) {
            onMarkerClick(destMarker);
            Location.distanceBetween(myLatlng.latitude, myLatlng.longitude,
                    destMarker.getPosition().latitude, destMarker.getPosition().longitude, result);
            Toast toast = Toast.makeText(getApplicationContext(), result[0] + " meters to this COIN"
                    , Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    ////////////////////Above IS Calculating Direction//////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////FOLLOWING IS Location Permission////////////////////////////////////////////////

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapGameMapView.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    ////////////////////Above IS Location Permission////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////FOLLOWING IS Requesting Direction from Google///////////////////////////////////

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "mode=walking";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exceptiondownloadingurl", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private AlertDialog mDialog;

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            if (result != null) {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.BLACK);
                }
                // Drawing polyline in the Google Map for the i-th route
                mGoogleMap.addPolyline(lineOptions);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapGameMapView.this);
                builder.setMessage("NO INTERNET CONNECTION!!!");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label_wordgame,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }
        }
    }


    ////////////////////Above IS Requesting Direction from Google///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onRestart() {
        super.onRestart();
        onConnected(Bundle.EMPTY);
    }


    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        if (isSensorPresent) {
            mSensorManager.unregisterListener(this);
        }

        saveUserList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String mStepsSinceReboot = (String.valueOf(event.values[0]));;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}