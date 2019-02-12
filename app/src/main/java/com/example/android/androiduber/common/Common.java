package com.example.android.androiduber.common;

import com.example.android.androiduber.remote.IGoogleApi;
import com.example.android.androiduber.remote.RetrofitClient;

public class Common {

    public static final String base_url = "https://maps.googleapis.com";

    public static IGoogleApi getGoogleApi() {

        return RetrofitClient.getClient(base_url).create(IGoogleApi.class);
    }
}
