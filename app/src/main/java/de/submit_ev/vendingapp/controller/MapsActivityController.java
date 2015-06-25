package de.submit_ev.vendingapp.controller;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.submit_ev.vendingapp.MapsActivity;
import de.submit_ev.vendingapp.R;
import de.submit_ev.vendingapp.api.ServerApi;
import de.submit_ev.vendingapp.events.MarkerUnselectedEvent;
import de.submit_ev.vendingapp.events.SelectedVendorChangedEvent;
import de.submit_ev.vendingapp.helper.GpsHelper;
import de.submit_ev.vendingapp.models.Vendor;

/**
 * Created by Igor on 28.05.2015.
 */
public class MapsActivityController {

    private MapsActivity mapsActivity;
    private GoogleMap mMap;
    private GpsHelper gpsHelper;
    private Map<Marker, Vendor> displayedVendors;

    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    public MapsActivityController(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;

        gpsHelper = new GpsHelper(mapsActivity.getApplicationContext());
        gpsHelper.setLocationListener(locationListener);

        mMap = mapsActivity.getMap();
        displayedVendors = new HashMap<>();

        ButterKnife.inject(this, mapsActivity);

        setup();

        EventBus.getDefault().register(this);
    }

    void setup() {
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                VisibleRegion bounds = mMap.getProjection().getVisibleRegion();
                if (cameraPosition.zoom > 12) {
                    double minLat = minValue(bounds.farLeft.latitude, bounds.farRight.latitude, bounds.nearRight.latitude, bounds.nearLeft.latitude);
                    double maxLat = maxValue(bounds.farLeft.latitude, bounds.farRight.latitude, bounds.nearRight.latitude, bounds.nearLeft.latitude);

                    double minLng = minValue(bounds.farLeft.longitude, bounds.farRight.longitude, bounds.nearRight.longitude, bounds.nearLeft.longitude);
                    double maxLng = maxValue(bounds.farLeft.longitude, bounds.farRight.longitude, bounds.nearRight.longitude, bounds.nearLeft.longitude);

                    updateDisplayedVendors(minLat, maxLat, minLng, maxLng);
                }
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (displayedVendors.get(marker) != null) {
                    Vendor vendor = displayedVendors.get(marker);
                    EventBus.getDefault().post(new SelectedVendorChangedEvent(vendor));
                    slidingUpPanelLayout.setTouchEnabled(true);
                }
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                EventBus.getDefault().post(new MarkerUnselectedEvent());
                slidingUpPanelLayout.setTouchEnabled(false);
            }
        });
    }

    double minValue(double... values) {
        if (values.length == 0)
            throw new IllegalArgumentException();
        double temp = values[0];
        for (double value : values)
            if (value < temp)
                temp = value;
        return temp;
    }

    double maxValue(double... values) {
        if (values.length == 0)
            throw new IllegalArgumentException();
        double temp = values[0];
        for (double value : values)
            if (value > temp)
                temp = value;
        return temp;
    }

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
            setMapPostitionAndZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16);
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Standort").draggable(false));
            gpsHelper.stopGpsService();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    @OnClick(R.id.buttonLocationSearch)
    void getLocation() {
        Toast.makeText(mapsActivity, "Search Location", Toast.LENGTH_SHORT).show();
        gpsHelper.startGpsService();
    }

    void setMapPostitionAndZoom(LatLng target, float zoomLevel) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, zoomLevel));
    }

    void updateDisplayedVendors(double lat1, double lat2, double lng1, double lng2) {
        ServerApi.getVendors(lat1, lat2, lng1, lng2, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new GsonBuilder().create();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        Vendor vendor = gson.fromJson(item.toString(), Vendor.class);

                        boolean alreadyDisplayed = false;
                        for (Marker marker : displayedVendors.keySet()) {
                            if (marker.getPosition().latitude == vendor.getLatitude() && marker.getPosition().longitude == vendor.getLongitude()) {
                                alreadyDisplayed = true;
                                break;
                            }
                        }
                        if (!alreadyDisplayed) {
                            displayedVendors.put(mMap.addMarker(new MarkerOptions().position(new LatLng(vendor.getLatitude(), vendor.getLongitude())).draggable(false)), vendor);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (statusCode == 0) {
                    // No internet connection
                } else {

                }
            }
        });
    }

    public void onEvent(MarkerUnselectedEvent event) {
        Toast.makeText(mapsActivity, "Unselected", Toast.LENGTH_SHORT).show();
    }

    public void onEvent(SelectedVendorChangedEvent event) {
        Toast.makeText(mapsActivity, "Selected", Toast.LENGTH_SHORT).show();
    }

}
