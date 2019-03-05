package com.example.android.androiduber.remote;

import com.example.android.androiduber.model.FCMResponse;
import com.example.android.androiduber.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAmmxHDRE:APA91bExyksKs49U4LhdiVh1gzrYg7eV1jA58nl13uoNAw7UuSca2FCzL-Ny6BuidFf1UDCpfmo4f6rFsjN-A9t8ROnQSTVN9MQZOTyS9IlGMuvBONPkAQGSlqNJQCu0Wxl4CRnBWrzY"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
