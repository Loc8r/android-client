package com.loc8r.biketrack;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mickeydang on 2017-09-16.
 */

public class User {
    private Map<String,LatLng> locationHistory;

    public Map<String, LatLng> getLocationHistory() {
        return locationHistory;
    }

    public void setLocationHistory(Map<String, LatLng> locationHistory) {
        this.locationHistory = locationHistory;
    }
}
