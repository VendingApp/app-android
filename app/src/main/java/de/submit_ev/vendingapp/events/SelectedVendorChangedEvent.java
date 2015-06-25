package de.submit_ev.vendingapp.events;

import de.submit_ev.vendingapp.models.Vendor;

/**
 * Created by Igor on 18.06.2015.
 */
public class SelectedVendorChangedEvent {
    Vendor vendor;

    public SelectedVendorChangedEvent(Vendor vendor) {
        this.vendor = vendor;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}
