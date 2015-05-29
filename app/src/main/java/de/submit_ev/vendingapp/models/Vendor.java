package de.submit_ev.vendingapp.models;

/**
 * Created by Igor on 07.05.2015.
 */
public class Vendor {
    long id;

    double longitude;
    double latitude;

    String address;
    String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public Vendor setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Vendor setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Vendor setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Vendor setDescription(String description) {
        this.description = description;
        return this;
    }
}
