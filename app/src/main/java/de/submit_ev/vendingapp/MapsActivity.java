package de.submit_ev.vendingapp;

import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.submit_ev.vendingapp.controller.MapsActivityController;
import de.submit_ev.vendingapp.helper.GpsHelper;
import de.submit_ev.vendingapp.helper.Preferences;
import de.submit_ev.vendingapp.models.Vendor;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MapsActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.inject(this);
        setUpMapIfNeeded();
        controller = new MapsActivityController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        restoreMapCoordinates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveMapCoordinates();
    }

    void saveMapCoordinates() {
        CameraPosition position = mMap.getCameraPosition();
        Preferences preferenceHelper = new Preferences(this);
        preferenceHelper.getEditor()
                .putFloat("map.longitude", (float) position.target.longitude)
                .putFloat("map.latitude", (float) position.target.latitude)
                .putFloat("map.zoom", position.zoom)
                .putFloat("map.bearing", position.bearing)
                .putFloat("map.tilt", position.tilt).commit();
    }

    void restoreMapCoordinates() {
        Preferences preferenceHelper = new Preferences(this);
        LatLng coordinates = new LatLng(preferenceHelper.getSharedPreferences().getFloat("map.latitude", 0),
                preferenceHelper.getSharedPreferences().getFloat("map.longitude", 0));
        float bearing = preferenceHelper.getSharedPreferences().getFloat("map.bearing", 0);
        float zoom = preferenceHelper.getSharedPreferences().getFloat("map.zoom", 0);
        float tilt = preferenceHelper.getSharedPreferences().getFloat("map.tilt", 0);

        CameraPosition position = new CameraPosition(coordinates, zoom, tilt, bearing);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
    }

    LatLngBounds generateBoundsFromList(List<Vendor> vendors) {
        double maxLat = Double.MIN_VALUE;
        double maxLng = Double.MIN_VALUE;
        double minLat = Double.MAX_VALUE;
        double minLng = Double.MAX_VALUE;
        for (Vendor vendor : vendors) {
            maxLat = Math.max(maxLat, vendor.getLatitude());
            maxLng = Math.max(maxLng, vendor.getLongitude());
            minLat = Math.min(minLat, vendor.getLatitude());
            minLng = Math.min(minLng, vendor.getLongitude());
        }
        LatLngBounds output = new LatLngBounds(new LatLng(minLat, minLng), new LatLng(maxLat, maxLng));
        return output;
    }

    public GoogleMap getMap() {
        return mMap;
    }
}
