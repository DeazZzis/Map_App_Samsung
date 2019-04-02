package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewPostActivity extends AppCompatActivity {

    private String title, text;
    private String dis;
    private double D;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private EditText edTitle, edText;
    private Intent intent;
    private double myLat, myLng, lat, lng;
    private LatLng latLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        final Intent receiveIntent = this.getIntent();

        edText = findViewById(R.id.editText);
        edTitle = findViewById(R.id.editTitle);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        GPSTracker gpsTracker = new GPSTracker(this);
        myLat = gpsTracker.getLatitude();
        myLng = gpsTracker.getLongitude();

        lat = receiveIntent.getDoubleExtra("lat", myLat);
        lng = receiveIntent.getDoubleExtra("lng", myLng);


        latLng = new LatLng(myLat, myLat);


        MapFragment mapFragment = new MapFragment(latLng, 14);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout3, mapFragment).commit();

        View view = findViewById(R.id.viewButton);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPostActivity.this,
                        MapForPostsActivity.class));
                finish();
            }
        });

        Button button = findViewById(R.id.buttonCreatePost);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          Post post = new Post();
                                          post.setAuthor(mAuth.getUid().toString());
                                          post.setLat(String.valueOf(lat));
                                          post.setLng(String.valueOf(lng));
                                          post.setText(edText.getText().toString());
                                          post.setTime(getCurrentTime());
                                          post.setTitle(edTitle.getText().toString());
                                          myRef.child("posts").push().setValue(post);

                                          startActivity(new Intent(NewPostActivity.this,
                                                  MapsActivity.class));
                                          finish();
                                      }
                                  }
        );
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("DDD:HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

}
