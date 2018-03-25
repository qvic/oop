package com.labs.vic.labspeedometer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final int MIN_TIME = 1000;
    public static final int MIN_DISTANCE = 0;

    private TextView textMessage;
    private TextView textMessage2;

    private TextView display1;
    private TextView display2;

    private Location previousLocation;
    private LocationManager locationManager;

//    private Handler handler;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    textMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_units:
                    textMessage.setText(R.string.title_units);
                    return true;
                case R.id.navigation_gps:
                    startActivity(new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    return false;
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Location", "No permission");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        Log.i("Location", "Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        Log.i("Location", "Paused");
    }

    private void updateView(final Location location) {
        textMessage.setText(String.format(Locale.getDefault(), "%s: %f",
                getString(R.string.title_latitude), location.getLatitude()));
        textMessage2.setText(String.format(Locale.getDefault(), "%s: %f",
                getString(R.string.title_longitude), location.getLongitude()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textMessage = findViewById(R.id.message);
        textMessage2 = findViewById(R.id.message2);
        display1 = findViewById(R.id.display1);
        display2 = findViewById(R.id.display2);
        BottomNavigationView navigationView = findViewById(R.id.navigation);

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

//        handler = new Handler();
    }

//    private void updateStatus() {
//    }

    @Override
    public void onLocationChanged(final Location location) {
        if (location == null) {
            Log.i("Location", "Not available");
            return;
        }

        updateView(location);

        if (previousLocation != null) {
            double distance = previousLocation.distanceTo(location);
            double timeElapsed = location.getTime() - previousLocation.getTime();

            double speed = Math.round(1000 * distance/timeElapsed) / 1000;
            double speed2 = location.getSpeed();

            Log.i("Location", "Speed: " + speed + " m/s");
            Log.i("Location", "Speed2: " + speed2 + " m/s");

            display1.setText(String.format(Locale.getDefault(), "%.3f m/s", speed));
            display2.setText(String.format(Locale.getDefault(), "%.1f m/s", speed2));
        }

        Log.i("Location", "Latitude: " + location.getLatitude() +
                ", Longitude: " + location.getLongitude());

        previousLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.i("Location", "Status: " + s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i("Location", "Enabled: " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i("Location", "Disabled: " + s);
    }
}
