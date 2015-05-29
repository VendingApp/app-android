package de.submit_ev.vendingapp.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Igor on 07.05.2015.
 */
public class GpsHelper {

    // Define a listener that responds to location updates
    LocationListener locationListener;
    LocationManager locationManager;

    public GpsHelper(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public void startGpsService() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void stopGpsService() {
        locationManager.removeUpdates(locationListener);
    }
}
