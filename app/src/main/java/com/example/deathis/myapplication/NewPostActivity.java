package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.deathis.myapplication.Fragments.MapFragment;
import com.example.deathis.myapplication.Models.Post;
import com.example.deathis.myapplication.Models.Rep;
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
        title = receiveIntent.getStringExtra("title");
        text = receiveIntent.getStringExtra("text");

        edText.setText(text);
        edTitle.setText(title);

        latLng = new LatLng(lat, lng);


        MapFragment mapFragment = new MapFragment(latLng, 13);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout3, mapFragment).commit();

        View view = findViewById(R.id.viewButton);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, MapForPostsActivity.class);
                intent.putExtra("title", edTitle.getText().toString());
                intent.putExtra("text", edText.getText().toString());
                startActivity(intent);
            }
        });

        Button button = findViewById(R.id.buttonCreatePost);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          int i = 0;
                                          if (edTitle.getText().toString().equals("")) {
                                              i += 1;
                                              edTitle.setError("Це поле не може бути пустим!");
                                          }
                                          if (edText.getText().toString().equals("")) {
                                              i += 1;
                                              edText.setError("Це поле не може бути пустим!");
                                          }
                                          if (i == 0) {
                                              Post post = new Post();
                                              Rep rep = new Rep();
                                              rep.setAuth("test");
                                              ArrayList<Rep> arrayList = new ArrayList<Rep>();
                                              arrayList.add(rep);
                                              post.setAuthor(mAuth.getUid().toString());
                                              post.setLat(String.valueOf(lat));
                                              post.setLng(String.valueOf(lng));
                                              post.setText(edText.getText().toString());
                                              post.setTime(getCurrentTime());
                                              post.setTitle(edTitle.getText().toString());
                                              post.setRep_down(arrayList);
                                              post.setRep_up(arrayList);
                                              myRef.child("posts").push().setValue(post);

                                              startActivity(new Intent(NewPostActivity.this,
                                                      MapsActivity.class));
                                              finish();
                                          }
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
