package com.example.android.androiduber.common;

import android.location.Location;

import com.example.android.androiduber.remote.FCMClient;
import com.example.android.androiduber.remote.IFCMService;
import com.example.android.androiduber.remote.IGoogleApi;
import com.example.android.androiduber.remote.RetrofitClient;

public class Common {

    public static final String token_tbl = "Tokens";
    public static final String driver_tbl = "Drivers";
    public static final String usr_driver_tbl = "DriversInfo";
    public static final String usr_rider_tbl = "RidersInfo";
    public static final String pickup_request_tbl = "PickupRequest";

    public static final String base_url = "https://maps.googleapis.com";
    public static final String fcm_url = "https://fcm.googleapis.com";

    public static Location mLocation = null;

    public static IGoogleApi getGoogleApi() {

        return RetrofitClient.getClient(base_url).create(IGoogleApi.class);
    }

    public static IFCMService getIFCMService() {

        return FCMClient.getClient(fcm_url).create(IFCMService.class);
    }
}
