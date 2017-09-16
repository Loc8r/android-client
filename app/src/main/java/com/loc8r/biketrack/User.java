package com.loc8r.biketrack;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by mickeydang on 2017-09-16.
 */

public class User {
    private String mAcctEmail;
    private String mUniqueID;
    private ArrayList<LatLng> mLatLngList;

    public String getAcctEmail() {
        return mAcctEmail;
    }

    public void setAcctEmail(String acctEmail) {
        mAcctEmail = acctEmail;
    }

    public String getUniqueID() {
        return mUniqueID;
    }

    public void setUniqueID(String uniqueID) {
        mUniqueID = uniqueID;
    }

    public ArrayList<LatLng> getLatLng() {
        return mLatLngList;
    }

    public void setLatLng(ArrayList<LatLng> latLng) {
        mLatLngList = latLng;
    }
}
