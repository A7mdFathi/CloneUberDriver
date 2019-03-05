package com.example.android.androiduber.service;

import android.content.Intent;
import android.util.Log;

import com.example.android.androiduber.CustomerCallActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Objects;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        LatLng customerLocation = new Gson().fromJson(remoteMessage.getNotification().getBody(), LatLng.class);

        Intent intent = new Intent(getBaseContext(), CustomerCallActivity.class);
        intent.putExtra("lat", customerLocation.latitude);
        intent.putExtra("lng", customerLocation.longitude);
        intent.putExtra("customer", remoteMessage.getNotification().getTitle());
        startActivity(intent);
    }
}
