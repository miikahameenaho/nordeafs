package com.example.mixu.nordeafs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by mixu on 14.12.2014.
 *
 * Singleton class for LocationTracker.
 *
 * Provides GPS based location service
 *
 * TODO: no support for network based location at the moment
 *
 */
public final class LocationTracker implements LocationListener {

    private final Context context;
    private Location location = null;
    private boolean isGPSEnabled = false;
    private boolean isLocationAvailable = false;
    private double latitude = 0.0;
    private double longitude = 0.0;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // 50 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 5000; // 5 seconds

    protected LocationManager locationManager;
    private static LocationTracker ourInstance = null;

    public static LocationTracker getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new LocationTracker(context);
        }
        return ourInstance;
    }

    private LocationTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.d("isGPSEnabled", "=" + isGPSEnabled);

            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                this.isLocationAvailable = false;
                location = null;

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("LocationTracker", "latitude: " + latitude + ", longitude: " + longitude);
                        this.isLocationAvailable = true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationTracker.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean isLocationAvailable() {
        return this.isLocationAvailable;
    }

    /*
        Method for showing Alert dialog in case when GPS is disabled in the device
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS disabled");

        // Setting Dialog Message
        alertDialog.setMessage("GPS seems to be disabled. Do you want to go to settings menu and enable it?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationTracker: ", "onLocationChanged");
        if (ourInstance == null) {
            ourInstance = new LocationTracker(context);
        }
        ourInstance.getLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("LocationTracker: ", "onProviderDisabled");
        isLocationAvailable = false;
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("LocationTracker: ", "onProviderEnabled");
        if (ourInstance == null) {
            ourInstance = new LocationTracker(context);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LocationTracker: ", "onStatusChanged");
    }

}
