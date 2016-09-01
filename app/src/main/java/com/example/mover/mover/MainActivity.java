package com.example.mover.mover;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements getRequest.AsyncResponse, OnMapReadyCallback {

    TextView tv1 = null;
    private double acc;
    private SensorManager mSensorManager;
    private String acceleration;
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    Location myPosition;
    private String coordinates;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = Mover.getUser();

        tv1 = (TextView) findViewById(R.id.sensorText);

        // Getting reference to the SupportMapFragment of activity_main.xml
        MapFragment mFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);


        //Accelerometer
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                acc = Math.sqrt(x * x + y * y + z * z);

                myPosition = getMyLocation();
                coordinates = "lat: " + myPosition.getLatitude() + "\nlong: " + myPosition.getLongitude();

                tv1.setText("Acceleration: " + acc + "\nGPS coordinates:\n" + coordinates);
                //tv1.setVisibility(View.VISIBLE);

                if (acc > 20) {
                    System.out.println(acc);
                    accidentAlert();
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

        }, sensor, SensorManager.SENSOR_DELAY_FASTEST);


    }

    public void accidentAlert() {

        CarAccident accident = new CarAccident(acc, myPosition.getLatitude(), myPosition.getLongitude());
        String request = "email=" + Mover.getUser() + "&lat=" + accident.getLat() + "&lng=" + accident.getLng() + "&acc=" + accident.getAcceleration();

        postRequest asyncTask = (postRequest) new postRequest(new postRequest.AsyncResponse() {

            @Override
            public void processFinish(String output) {
                Context context = getApplicationContext();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, output, duration);
                toast.show();
            }
        }, request).execute("http://139.162.178.79:4000/car_accident");

        Intent k = new Intent(this, accidentActivity.class);
        startActivity(k);

    }

    @Override
    public void processFinish(String output) {

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Dont have permission
        }
        googleMap.setMyLocationEnabled(true);
        myPosition = getMyLocation();
    }

    private Location getMyLocation() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //no permision
            }
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}
