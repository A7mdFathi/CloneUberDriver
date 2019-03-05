package com.example.android.androiduber;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.androiduber.common.Common;
import com.example.android.androiduber.model.DataNotify;
import com.example.android.androiduber.model.FCMResponse;
import com.example.android.androiduber.model.Sender;
import com.example.android.androiduber.model.Token;
import com.example.android.androiduber.remote.IFCMService;
import com.example.android.androiduber.remote.IGoogleApi;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCallActivity extends AppCompatActivity {

    TextView txtTime, txtAddress, txtDistance;

    Button btnAccept, btnDecline;

    MediaPlayer mediaPlayer;

    IGoogleApi mService;
    IFCMService mIfcmService;

    String customerId;

    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call);

        txtTime = (TextView) findViewById(R.id.txttime);
        txtAddress = (TextView) findViewById(R.id.txtaddress);
        txtDistance = (TextView) findViewById(R.id.txtdistance);

        btnAccept = (Button) findViewById(R.id.accept_btn);
        btnDecline = (Button) findViewById(R.id.decline_btn);

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBooking(customerId);
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerCallActivity.this, DriverTrackerActivity.class);

                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);

                startActivity(intent);
                finish();
            }
        });

        mService = Common.getGoogleApi();
        mIfcmService = Common.getIFCMService();

        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        if (getIntent() != null) {

            lat = getIntent().getDoubleExtra("lat", -1.0);
            lng = getIntent().getDoubleExtra("lng", -1.0);
            customerId = getIntent().getStringExtra("customer");

            getDirection(lat, lng);
        }

    }

    private void cancelBooking(String customerId) {
        Token token = new Token(customerId);

        DataNotify dataNotify = new DataNotify("notice!", "Driver cancel your request");
        Sender sender = new Sender(dataNotify, token.getToken());

        mIfcmService.sendMessage(sender)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1) {
                            Toast.makeText(CustomerCallActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });
    }

    private void getDirection(double lat, double lng) {


        String requestApi = null;
        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + Common.mLocation.getLatitude() + "," + Common.mLocation.getLatitude() + "&" +
                    "destination=" + lat + "," + lng + "&" +
                    "key=" + getResources().getString(R.string.google_maps_key);


            Log.d("EDMTDEV", requestApi);


            mService.getPath(requestApi)

                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().toString());

                                JSONArray routes = jsonObject.getJSONArray("routes");

                                JSONObject object = routes.getJSONObject(0);

                                JSONArray legs = object.getJSONArray("legs");

                                JSONObject legsObject = legs.getJSONObject(0);

                                JSONObject distance = legsObject.getJSONObject("distance");
                                txtDistance.setText(distance.getString("text"));

                                JSONObject time = legsObject.getJSONObject("duration");
                                txtTime.setText(time.getString("text"));

                                String address = legsObject.getString("end_address");
                                txtAddress.setText(address);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(CustomerCallActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        mediaPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();

    }
}
