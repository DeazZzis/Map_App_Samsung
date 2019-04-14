package com.example.deathis.myapplication.Fragments;

import com.example.deathis.myapplication.Notifications.MyResponse;
import com.example.deathis.myapplication.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA3OJRPOI:APA91bHpoR0gm-ZqAGT7zc2qEs2Uibsgx90hUmTtRo99IlMA0rZXijanDkhBMApR2wFZvU6GLvcsPebnmi2QLiNrjyHU3qleCwFfEOypXE8ADtQEOXtFIvYoDpHvxIpcWNVTpZLh2ntG"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
