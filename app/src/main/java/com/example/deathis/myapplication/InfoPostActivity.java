package com.example.deathis.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class InfoPostActivity extends AppCompatActivity {

    private String title, text;
    private float lat, lng;
    private String dis;
    private double D;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_post);


        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        text = bundle.getString("text");
        lat = Float.parseFloat(bundle.getString("lat"));
        lng = Float.parseFloat(bundle.getString("lng"));
        dis = bundle.getString("meters");
        D = Double.parseDouble(bundle.getString("D"));
        LatLng latLng = new LatLng(lat, lng);

        TextView textViewText = findViewById(R.id.textview_text);
        TextView textViewTitle = findViewById(R.id.title_info);
        TextView textViewMet = findViewById(R.id.meters_info);

        textViewTitle.setText(title);
        textViewText.setText(text);


        if (D != 0 && D < 20000) {
            textViewMet.setText(dis);
        } else {
            textViewMet.setText("");
        }

        MapFragment mapFragment = new MapFragment(latLng, 17);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout2, mapFragment).commit();

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }


}
